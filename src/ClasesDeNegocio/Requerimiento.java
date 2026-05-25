package ClasesDeNegocio;

public class Requerimiento {

    private int cantidadLideres;
    private int cantidadArquitectos;
    private int cantidadProgramadores;
    private int cantidadTesters;

    public Requerimiento(int cantidadLideres, int cantidadArquitectos, 
                         int cantidadProgramadores, int cantidadTesters) {
        this.cantidadLideres = cantidadLideres;
        this.cantidadArquitectos = cantidadArquitectos;
        this.cantidadProgramadores = cantidadProgramadores;
        this.cantidadTesters = cantidadTesters;
    }

    public int getCantidadPorRol(Rol rol) {
        switch (rol) {
            case LIDER_DE_PROYECTO: return cantidadLideres;
            case ARQUITECTO:        return cantidadArquitectos;
            case PROGRAMADOR:       return cantidadProgramadores;
            case TESTER:            return cantidadTesters;
            default:                return 0;
        }
    }

    public int getTotalPersonas() {
        return cantidadLideres + cantidadArquitectos + cantidadProgramadores + cantidadTesters;
    }

    // getters
    public int getCantidadLideres() { return cantidadLideres; }
    public int getCantidadArquitectos() { return cantidadArquitectos; }
    public int getCantidadProgramadores() { return cantidadProgramadores; }
    public int getCantidadTesters() { return cantidadTesters; }
}
