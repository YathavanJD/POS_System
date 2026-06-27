#!/bin/bash
# Java POS System - Build & Run Script

echo "========================================"
echo "  Java POS System - Build Script"
echo "========================================"

# Check Java
if ! command -v javac &> /dev/null; then
    echo "[ERROR] Java JDK not found. Please install Java 17+ and try again."
    exit 1
fi

JAVA_VER=$(java -version 2>&1 | head -1 | grep -oP '(?<=version ")\d+')
echo "[INFO] Java version: $JAVA_VER"

# Create output dir
mkdir -p out

# Compile
echo "[INFO] Compiling..."
find src -name "*.java" > sources.txt
javac -d out @sources.txt

if [ $? -ne 0 ]; then
    echo "[ERROR] Compilation failed."
    rm -f sources.txt
    exit 1
fi

rm -f sources.txt
echo "[OK] Compilation successful."

# Create JAR
echo "[INFO] Packaging JAR..."
echo "Main-Class: pos.Main" > manifest.txt
jar cfm JavaPOS.jar manifest.txt -C out .
rm -f manifest.txt

echo "[OK] JavaPOS.jar created."
echo ""
echo "  To run: java -jar JavaPOS.jar"
echo ""

# Auto-run?
read -p "Run the application now? (y/n): " yn
if [[ "$yn" == "y" || "$yn" == "Y" ]]; then
    java -jar JavaPOS.jar
fi
