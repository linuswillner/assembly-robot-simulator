# assembly-robot-simulator

Course project built for the Ohjelmointiprojekti TX00CD79-3014 course at Metropolia UAS.

# Running in production

Access to a [MariaDB instance](https://mariadb.org) is required for running this application.

```bash
# Set required environment variables
# (On Windows: export => set)
export DB_HOST=localhost 
export DB_PORT=3306
export DB_USERNAME=root
export DB_PASSWORD=password

# Run the application
java -jar assembly-robot-simulator.jar
```

Should you at any time wish to reset all stored settings of the simulator (**Note: This includes the database!**), you can supply the `FRESH_INSTALL` environment variable.

# Development

In development environments, environment variables can be set via the IntelliJ Run Configurations menu (`Run -> Edit Configurations -> Application -> Environment Variables -> Click the "$" or type manually`).

## Required build tools

### JavaFX

Running of this project requires [JavaFX](https://openjfx.io) to be installed:

1. [Download the JavaFX SDK](https://gluonhq.com/products/javafx) (The
   project uses version 15.0.1) and extract to a path of your choosing
2. In IntelliJ, open Project Structure (`Ctrl + Alt + Shift + S`)
3. Go to `Project Settings -> Libraries -> + -> Java -> Select JavaFX SDK extraction path`
4. Go to `Run -> Edit Configurations -> Application -> Modify Options -> Tick 'Add VM options'`
5. Set the following VM options: `--module-path "path\to\javafx-sdk-15.0.1\lib" --add-modules javafx.controls,javafx.fxml`

### WiX Toolset

MSI and EXE workloads with JPackage require the [WiX Toolset](https://github.com/wixtoolset/wix3) to be installed.