package dev.germantovar.springboot.services;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import dev.germantovar.springboot.entities.User;
import dev.germantovar.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class UserService implements IUserService {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRepository repository;

    @Override
    public List<User> getAll() {
        return (List<User>) repository.findAll();
    }

    @Override
    public void save(User usuario) {
        repository.save(usuario);
    }

    @Override
    public User credenciales(User usuario) {
        String query = "FROM User WHERE name = :name";
        List<User> lista = entityManager.createQuery(query)
                .setParameter("name", usuario.getName())
                .getResultList();

        if (lista.isEmpty()) {
            return null;
        }

        String passwordHashed = lista.get(0).getPassword();
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        if (argon2.verify(passwordHashed, usuario.getPassword())) {
            return lista.get(0);
        }
        return null;
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id).orElse(null);
    }
}

