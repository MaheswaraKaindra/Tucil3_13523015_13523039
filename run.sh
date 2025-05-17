set -e

JFX_LIB="lib/mac"
JFX_NATIVE="lib/mac/native"

mkdir -p bin/main
javac --module-path "$JFX_LIB" --add-modules javafx.controls,javafx.fxml \
      -d bin/main $(find src/main/java -name "*.java")

cp src/main/resources/layout.fxml bin/main/

java --module-path "$JFX_LIB" --add-modules javafx.controls,javafx.fxml \
     -Djava.library.path="$JFX_NATIVE" \
     -cp bin/main app.Main

