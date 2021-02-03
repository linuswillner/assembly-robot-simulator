# assembly-robot-simulator

Course project built for the Ohjelmointiprojekti TX00CD79-3014 course at Metropolia UAS

# Development

## Required build tools

### JavaFX

Running of this project requires [JavaFX](https://openjfx.io) to be installed:

1. [Download the JavaFX SDK](https://gluonhq.com/products/javafx) and extract to a path of your choosing
2. In IntelliJ, open Project Structure (`Ctrl + Alt + Shift + S`)
3. Go to `Project Settings -> Libraries -> + -> Java -> Select JavaFX SDK extraction path`
4. Go to `Run -> Edit Configurations -> Application -> Modify Options -> Tick 'Add VM options'`
5. Set the following VM options: `--module-path "path\to\javafx-sdk-15.0.1\lib" --add-modules javafx.controls,javafx.fxml`

### WiX Toolset

MSI and EXE workloads with JPackage require the [WiX Toolset](https://github.com/wixtoolset/wix3) to be installed.