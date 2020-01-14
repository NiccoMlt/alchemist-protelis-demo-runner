import Main.load
import it.unibo.alchemist.AlchemistRunner
import it.unibo.alchemist.core.implementations.Engine
import it.unibo.alchemist.core.interfaces.Simulation
import it.unibo.alchemist.core.interfaces.Status
import it.unibo.alchemist.loader.Loader
import it.unibo.alchemist.loader.YamlLoader
import it.unibo.alchemist.loader.export.EnvPerformanceStats
import it.unibo.alchemist.loader.export.ExecutionTime
import it.unibo.alchemist.loader.export.Extractor
import it.unibo.alchemist.loader.export.NumberOfNodes
import it.unibo.alchemist.loader.export.Time
import it.unibo.alchemist.model.implementations.positions.Euclidean2DPosition
import it.unibo.alchemist.model.implementations.times.DoubleTime
import it.unibo.alchemist.model.interfaces.Environment
import it.unibo.alchemist.model.interfaces.Position2D
import java.util.concurrent.TimeUnit

fun main() {
    val engine = load<Any>()
    engine.play()
    engine.run()
    engine.waitFor(Status.TERMINATED, 0, TimeUnit.MILLISECONDS)
}

object Main {
    private const val simulationTime: Double = 10.0

    fun <T> load(): Simulation<T, Euclidean2DPosition> {
        val loader: Loader = YamlLoader(this.javaClass.classLoader.getResourceAsStream("demo.yml"))
        val env: Environment<T, Euclidean2DPosition> = loader.getDefault<T, Euclidean2DPosition>()
        val engine: Engine<T, Euclidean2DPosition> = Engine(env, DoubleTime(simulationTime))

        val extractors: MutableList<Extractor> = loader.dataExtractors.toMutableList()
        // TODO: why extractors variable is an empty list?
        extractors += EnvPerformanceStats()
        extractors += ExecutionTime()
        // extractors += MeanSquaredError() // todo
        // extractors += MoleculeReader() // todo
        extractors += NumberOfNodes()
        extractors += Time()
        val monitor: JsonOutputMonitor<T, Euclidean2DPosition> = getMonitor(extractors)
        engine.addOutputMonitor(monitor)

        return engine
    }

    @Deprecated("I think it's better to use Alchemist Engine directly, to avoid alchemist-runner dependency")
    fun <T, P : Position2D<P>> loadRunner() {
        val loader: Loader = YamlLoader(this.javaClass.classLoader.getResourceAsStream("demo.yml"))
        val simBuilder: AlchemistRunner.Builder<*, *> = AlchemistRunner.Builder<T, P>(loader).headless(true)
        simBuilder.build().launch()
    }

    private fun <T, P : Position2D<P>> getMonitor(extractors: List<Extractor>): JsonOutputMonitor<T, P> {
        return JsonOutputMonitor(printOutput = ::println, sampleSpace = 1.0, extractors = extractors)
    }
}
