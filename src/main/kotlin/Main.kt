import Main.load
import it.unibo.alchemist.core.implementations.Engine
import it.unibo.alchemist.core.interfaces.Status
import it.unibo.alchemist.loader.Loader
import it.unibo.alchemist.loader.YamlLoader
import it.unibo.alchemist.model.implementations.positions.Euclidean2DPosition
import it.unibo.alchemist.model.implementations.times.DoubleTime
import it.unibo.alchemist.model.interfaces.Environment
import it.unibo.alchemist.model.interfaces.Position2D
import java.util.concurrent.TimeUnit

fun main() {
    val engine = load<Any, Euclidean2DPosition>()
    engine.play()
    engine.run()
    engine.waitFor(Status.TERMINATED, 0, TimeUnit.MILLISECONDS)
}

object Main {
    private const val simulationTime: Double = 10.0

    fun <T, P : Position2D<P>> load(): Engine<T, Euclidean2DPosition> {
        val loader: Loader = YamlLoader(this.javaClass.classLoader.getResourceAsStream("demo.yml"))
//        val simBuilder: AlchemistRunner.Builder<*, *> = AlchemistRunner.Builder<T, P>(loader).headless(true)
//        simBuilder.build().launch()
        val env: Environment<T, Euclidean2DPosition> = loader.getDefault<T, Euclidean2DPosition>()
        return Engine(env, DoubleTime(simulationTime))
    }
}
