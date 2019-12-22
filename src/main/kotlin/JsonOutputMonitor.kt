import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import it.unibo.alchemist.boundary.interfaces.OutputMonitor
import it.unibo.alchemist.loader.export.Extractor
import it.unibo.alchemist.model.interfaces.Environment
import it.unibo.alchemist.model.interfaces.Position
import it.unibo.alchemist.model.interfaces.Reaction
import it.unibo.alchemist.model.interfaces.Time
import java.text.SimpleDateFormat
import java.util.*

@JsonClass(generateAdapter = true)
data class JsonOutputFormat(
    var startTime: String = "",
    var finishTime: String = "",
    var count: Long = -1,
    var steps: List<Map<String, Double>> = listOf()
)

/**
 * This class models a simple [OutputMonitor] for an
 * [Alchemist simulation Engine][it.unibo.alchemist.core.interfaces.Simulation]
 * that serializes everything on a JSON that is used somehow as a String.
 *
 * This class was built naïvely following what is done in the
 * [official file exporter][it.unibo.alchemist.loader.export.Exporter] implementation.
 *
 * @param extractors a list of [data extractors][Extractor] needed to retrieve infos
 * @param printOutput the function that uses the generated JSON at the end of the simulation
 * @param printEachStep if the monitor should invoke [printOutput] at each step
 * @param sampleSpace the sampling space, namely how many simulated time units the Exporter should log
 */
class JsonOutputMonitor<T, P : Position<P>>(
    private val printOutput: (json: String) -> Unit = ::println,
    private val sampleSpace: Double,
    private val extractors: List<Extractor>,
    private val printEachStep: Boolean = false
) : OutputMonitor<T, P> {
    private val isoTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", Locale.US)

    init {
        isoTime.timeZone = TimeZone.getTimeZone("UTC")
    }

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private var output = JsonOutputFormat()

    override fun initialized(env: Environment<T, P>) {
        output.startTime = isoTime.format(Date())
    }

    override fun stepDone(env: Environment<T, P>, r: Reaction<T>, time: Time, step: Long) {
        val curSample = (time.toDouble() / sampleSpace).toLong()
        if (curSample > output.count) {
            output.count = curSample
            writeRow(env, r, time, step)
            if (printEachStep) printDatum()
        }
    }

    /** Write environment state on output state object. */
    private fun writeRow(env: Environment<T, P>, r: Reaction<T>, time: Time, step: Long) {
        val map: Map<String, Double> = extractors
            .flatMap { it.names.zip(it.extractData(env, r, time, step).toTypedArray()) }
            .map { it.first to it.second }
            .toMap()
        output.steps += map
    }

    /** Apply [printOutput] function on current output state object. */
    private fun printDatum() {
        val json = moshi.adapter<JsonOutputFormat>(JsonOutputFormat::class.java).toJson(output)
        printOutput(json)
    }

    override fun finished(env: Environment<T, P>, time: Time, step: Long) {
        output.finishTime = isoTime.format(Date())
        printDatum()
    }
}
