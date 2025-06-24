# =================================================================
# Diseñado para (Linux/Windows)
# =================================================================

# Nombre del paquete y clase principal de la aplicación
MAIN_PACKAGE = cl.usm.gestorinventario
MAIN_CLASS = $(MAIN_PACKAGE).MainApp

# Directorios del proyecto
SRC_DIR = src
BIN_DIR = bin

# Comandos de Java
JAVAC = javac
JAVA = java

# --- Detección de Sistema Operativo y configuración de JavaFX ---

# Variables para la ruta de JavaFX y el separador de rutas
JFX_PATH = /opt/javafx-sdk-21.0.7/lib
PATH_SEPARATOR = :

# Si el sistema operativo es Windows_NT, se sobreescriben las variables
ifeq ($(OS),Windows_NT)
# En Windows, se usa una ruta diferente para JavaFX y el separador de rutas es diferente
    JFX_PATH = C:/java-libs/javafx-sdk-21.0.7/lib
    PATH_SEPARATOR = ;
endif

# Módulos de JavaFX que el proyecto necesita para funcionar
JFX_MODULES = javafx.controls,javafx.fxml


# --- Reglas de compilación y ejecución ---

# Regla por defecto que se ejecuta al escribir solo "make"
# Depende de la regla "compile", por lo que compilará primero.
all: compile

# Regla para compilar todos los archivos .java del directorio src
# y dejar los .class en el directorio bin.
compile:
	@echo "==> Compilando fuentes Java..."
	@mkdir -p $(BIN_DIR)
	$(JAVAC) --module-path "$(JFX_PATH)" --add-modules $(JFX_MODULES) -d $(BIN_DIR) $(SRC_DIR)/$(subst .,/,$(MAIN_PACKAGE))/*.java

# Regla para ejecutar la aplicación ya compilada
run:
	@echo "==> Ejecutando la aplicacion..."
	$(JAVA) --module-path "$(JFX_PATH)" --add-modules $(JFX_MODULES) -cp $(BIN_DIR) $(MAIN_CLASS)

# Regla para limpiar el proyecto, borrando los archivos compilados
clean:
	@echo "==> Limpiando archivos compilados del directorio $(BIN_DIR)..."
	@rm -rf $(BIN_DIR)/*


# Le indica a 'make' que 'all', 'compile', 'run' y 'clean' no son archivos,
# sino nombres de acciones a ejecutar.
.PHONY: all compile run clean