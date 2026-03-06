package br.edu.biblioteca.persistence;

import br.edu.biblioteca.exception.PersistenciaException;
import br.edu.biblioteca.model.Biblioteca;

import java.io.*;

public class PersistenciaService {
    private final File arquivo;

    public PersistenciaService(File arquivo) {
        this.arquivo = arquivo;
    }

    public Biblioteca carregar() throws PersistenciaException {
        if (!arquivo.exists()) return new Biblioteca();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            Object obj = ois.readObject();
            return (Biblioteca) obj;
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException("Falha ao carregar dados: " + arquivo.getAbsolutePath(), e);
        }
    }

    public void salvar(Biblioteca biblioteca) throws PersistenciaException {
        arquivo.getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(biblioteca);
        } catch (IOException e) {
            throw new PersistenciaException("Falha ao salvar dados: " + arquivo.getAbsolutePath(), e);
        }
    }
}
