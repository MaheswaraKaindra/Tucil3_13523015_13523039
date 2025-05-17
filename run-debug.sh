#!/bin/bash
set -e

JFX_LIB="lib/mac"
JFX_NATIVE="lib/mac/native"

SRC_DIR="src/main/java"
RES_DIR="src/main/resources"
OUT_DIR="bin/main"

mkdir -p "$OUT_DIR"

javac --module-path "$JFX_LIB" --add-modules javafx.controls,javafx.fxml \
      -d "$OUT_DIR" $(find "$SRC_DIR" -name "*.java") >/dev/null 2>&1

find "$RES_DIR" -type f \( -name "*.fxml" -o -name "*.css" -o -name "*.txt" \) \
    -exec cp {} "$OUT_DIR" \;

java --module-path "$JFX_LIB" --add-modules javafx.controls,javafx.fxml \
     -Djava.library.path="$JFX_NATIVE" \
     -cp "$OUT_DIR" app.App
