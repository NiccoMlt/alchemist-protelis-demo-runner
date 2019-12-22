# Alchemist Protelis engine

Demo project for a Protelis execution environment in Alchemist.

## Alchemis YAML

Alchemist uses YAML for its configurations.
The following syntax is what is needed to know to use it for Protelis.

### Incarnation

An [**incarnation**](https://alchemistsimulator.github.io/wiki/simulator/metamodel/#incarnations) of Alchemist 
is a concrete instance of the Alchemist meta-model;
it must define:

- Means for translating strings into named entities (**molecules**);
- Means for obtaining a number when given a node, a molecule and a string represent a property;
- Means for building incarnation-specific model entities given an appropriate context and a parameter String;

To use the Protelis incarnation, simply use

```yaml
incarnation: protelis
```

### Network model and node displacements

The `network-model` field defines how nodes are linked.
For example, the following configuration links nodes that are closer than 5 distance units:

```yaml
network-model:
  type: ConnectWithinDistance
  parameters: [5]
```

The `displacements` field accepts a YAML array of displacement objects describing how nodes are positioned.
For example, these are some configurations:

**As a [Point](https://www.javadoc.io/doc/it.unibo.alchemist/alchemist-loading/latest/it/unibo/alchemist/loader/displacements/Point.html)**
```yaml
# In this section the nodes are described
displacements:
  # A list of "in" entries, where each entry defines a group of nodes
  - in:
      # Uses the class it.unibo.alchemist.loader.displacements.Point
      type: Point
      # Using a constructor taking as input a couple of integers
      parameters: [0, 0]
  - in:
      type: Point
      parameters: [0, 1]
```

**As a [Circle](https://www.javadoc.io/static/it.unibo.alchemist/alchemist-loading/latest/it/unibo/alchemist/loader/displacements/Circle.html)**
```yaml
displacements:
  - in:
      # Uses the class it.unibo.alchemist.loader.displacements.Circle
      type: Circle
      # 10000 nodes, displaced in a circle with center in (0, 0) and radius 10
      parameters: [10000, 0, 0, 10]
```

**As a [Grid](https://www.javadoc.io/static/it.unibo.alchemist/alchemist-loading/latest/it/unibo/alchemist/loader/displacements/Grid.html)**
```yaml
displacements:
  - in:
      # Regular grid of nodes
      type: Grid
      # Starting from (-5, -5), ending in (5, 5), with nodes every (0.25, 0.25) and no randomness
      parameters: [-5, -5, 5, 5, 0.25, 0.25, 0, 0]
```

#### Node sensors, molecules and concentrations

##### Molecule

A [**molecule**](https://alchemistsimulator.github.io/it/unibo/alchemist/model/interfaces/Molecule/) is 
the name of a data item;
if Alchemist were an imperative programming language, a molecule would be the concept of variable name.

In the Protelis incarnation of Alchemist, a molecule refers to a **sensor**.

##### Concentration

The [**concentration**](https://alchemistsimulator.github.io/it/unibo/alchemist/model/interfaces/Concentration/) 
of a molecule is the value associated to a particular molecule;
if Alchemist were an imperative programming language, a concentration would be the concept of value
associated to a variable.

In the Protelis incarnation of Alchemist, a concentration of a molecule refers to the **value** of that sensor.
Any valid stateless (no rep or nbr) Protelis program can be entered as concentration value.
It will be fed to the ProtelisIncarnation, that in turn will interpret it, extract a result, and inject it in the node.

##### Node sensors

The `in` key is used to restrict the range where a sensor is injected.
If omitted, the injection will happen in every node.

An example:

```yaml
displacements:
  - in:
      type: Grid
      parameters: [-5, -5, 5, 5, 0.25, 0.25, 0.1, 0.1]
    # In the content section, we configure the node sensors
    contents:
      # Molecule, in the Protelis incarnation of alchemist, just means "global variable",
      # or, better suiting the aggregate programming metaphor, sensor name.
      - molecule: hello
      # Concentration is the sensor value. Any valid stateless (no rep or nbr) protelis program
      # can be entered as concentration value. It will be fed to the ProtelisIncarnation, that in
      # turn will interpret it, extract a result, and inject it in the node.
      # Note that, if you want to inject a string, you must escape the double quotes!
        concentration: "\"Hello, world!\""
      # The `in` key is used to restrict the range where a sensor is injected. If omitted, the
      # injection will happen in every node. 
      - in:
          # Loads an instance of it.unibo.alchemist.loader.shapes.Rectangle
          type: Rectangle
          # A square starting from (-1, -1) with sides 2 distance units large
          parameters: [-1, -1, 2, 2]
      # This will be the sensor name
        molecule: value
      # This will be fed to the interpreter, evaluated to the PI value, then bound to the sensor
        concentration: PI
      - in:
          type: Rectangle
          parameters: [-6, -6, 2, 2]
        molecule: value
      # Java imports and method calls are allowed!
      # Which mean that yes, you can ruin reproducibility of your simulation in arbitrary ways.
      # Handle with care.
        concentration: >
          import java.lang.Math.random
          random() * PI
```

#### Gradients and programs

Under a `programs` can be defined a list of programs that will be loaded by the interpreter;
for example:

```yaml
# Let's use the YAML variables to isolate the behavior definition
# It is not mandatory.
gradient: &gradient
  # Defines the frequency at which the program will be executed.
  # If no explicit class is specified, by defaults it uses a Dirac Comb.
  - time-distribution: 1
    # The Protelis program can be written directly here
    # The Protelis syntax won't be covered by this tutorial
    program: >
      def aFunction() {
        1
      }
      aFunction() * self.nextRandomDouble()
  # This line enables sending the result of the computation to neighbors.
  # It is mandatory, and it allows a fine grained control on the timing, in
  # case network lag simulation is required. By default, it sends immediately
  # after execution, in no time.
  - program: send

displacements:
  - in:
      type: Grid
      parameters: [-5, -5, 5, 5, 0.25, 0.25, 0.1, 0.1]
    programs:
      # Reference to the "gradient" list of programs
      - *gradient
```

Because writing Protelis programs within a YAML file is a bad experience
some characters need escaping, no syntax highlight, no code suggestions...), it is recommended to use a Protelis-aware
editor to write your code, then loading the code as a module within Alchemist.
Just make sure that external code is part of the classpath (ie. src/main/protelis).

For example:

```yaml
gradient: &gradient
  - time-distribution: 1
    program: org:protelis:tutorial:distanceTo
  - program: send

displacements:
  - in:
      type: Grid
      parameters: [-5, -5, 5, 5, 0.25, 0.25, 0.1, 0.1]
    contents:
      - in:
          type: Rectangle
          parameters: [-6, -6, 2, 2]
        molecule: source
        concentration: true
    programs:
      - *gradient
```

## Credits

All the examples used in this README are adaptations of the ones used in
[`Protelis-Incarnation-tutorial` repo](https://github.com/AlchemistSimulator/Protelis-Incarnation-tutorial)
of Alchemist Simulator official GitHub profile.

Further references about Alchemist metamodel can be found on
[official website](https://alchemistsimulator.github.io/wiki/simulator/metamodel/).

Some other examples of Protelis usage without Alchemist simulator can be found on the 
official [`Protelis-Demo` repo](https://github.com/Protelis/Protelis-Demo).
