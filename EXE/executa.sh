#!/bin/bash

# 1. Ens movem a la carpeta EXE (on està l'script)
cd "$(dirname "$0")"

# 2. Saltem a la carpeta FONTS
cd ..
cd FONTS

# 3. Verifiquem que el fitxer existeix
if [ ! -f "gradlew" ]; then
    echo "[ERROR] No trobo el fitxer 'gradlew' a FONTS."
    exit 1
fi

# 4. Assegurem permisos d'execució (+x)
chmod +x gradlew

# 5. Executem (run)
echo "[INFO] Executant..."
./gradlew run

# 6. Pausa final
echo ""
echo "Prem ENTER per sortir..."
read input