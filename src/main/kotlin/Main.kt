import Main.load
import it.unibo.alchemist.AlchemistRunner
import it.unibo.alchemist.loader.Loader
import it.unibo.alchemist.loader.YamlLoader
import it.unibo.alchemist.model.implementations.positions.Euclidean2DPosition
import it.unibo.alchemist.model.interfaces.Position2D

fun main() {
    load<Any, Euclidean2DPosition>()
}

object Main {
    fun <T, P : Position2D<P>> load() {
        val loader: Loader = YamlLoader(this.javaClass.classLoader.getResourceAsStream("demo.yml"))
        val simBuilder: AlchemistRunner.Builder<*, *> = AlchemistRunner.Builder<T, P>(loader).headless(true)
        simBuilder.build().launch()
    }
}
