# =================================================================
# Makefile Robusto para JavaFX (Linux/Windows)
# =================================================================

# Nombre del paquete y clase principal de la aplicación
MAIN_PACKAGE = cl.usm.gestorinventario
MAIN_CLASS = $(MAIN_PACKAGE).MainApp

# Directorios del proyecto
SRC_DIR = src
# ======================== CAMBIO PARA RECURSOS ========================
# Se definen los directorios de compilación (bin) y de recursos (FXML, etc.)
RESOURCES_DIR = $(SRC_DIR)/main/resources
BIN_DIR = bin

# Comandos de Java
JAVAC = javac
JAVA = java

# --- Detección de Sistema Operativo y configuración ---

JFX_PATH = /opt/javafx-sdk-21.0.7/lib
PATH_SEPARATOR = :
# Busca automáticamente TODOS los archivos .java dentro de la carpeta src.
SOURCES = $(shell find $(SRC_DIR) -name "*.java")

ifeq ($(OS),Windows_NT)
    # Sobrescribir para Windows
    JFX_PATH = C:/javafx/javafx-sdk-21.0.7/lib
    PATH_SEPARATOR = ;
    # Comando de Windows para buscar todos los archivos .java recursivamente.
    SOURCES = $(shell dir $(SRC_DIR)\\*.java /s /b)
endif

# Módulos de JavaFX que el proyecto necesita
JFX_MODULES = javafx.controls,javafx.fxml

# --- Reglas de compilación y ejecución ---

.PHONY: all compile run clean

# Regla por defecto
all: run

# Regla para compilar
compile:
	@echo "==> Compilando fuentes Java..."
	@echo "Archivos encontrados: $(SOURCES)" # Línea de depuración para ver qué archivos se encontraron
	@mkdir -p $(BIN_DIR)
	$(JAVAC) --module-path "$(JFX_PATH)" --add-modules $(JFX_MODULES) -d $(BIN_DIR) $(SOURCES)

# Regla para ejecutar
run: compile
	@echo "==> Ejecutando la aplicacion..."
# ======================== CAMBIO PARA RECURSOS ========================
# Se añade el directorio de recursos al classpath (-cp) para que Java
# pueda encontrar los archivos FXML al ejecutar la aplicación.
	$(JAVA) --module-path "$(JFX_PATH)" --add-modules $(JFX_MODULES) -cp "$(BIN_DIR)$(PATH_SEPARATOR)$(RESOURCES_DIR)" $(MAIN_CLASS)

# Regla para limpiar
clean:
	@echo "==> Limpiando el directorio $(BIN_DIR)..."
ifeq ($(OS),Windows_NT)
	@if exist $(BIN_DIR) rd /s /q $(subst /,\,$(BIN_DIR))
else
	@rm -rf $(BIN_DIR)
endif