/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ifpb.pd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author matheus
 */
@Stateless(mappedName = "gerenciador")
public class GerenciadorSB implements GerenciadorRemote {
    
    @PersistenceContext(unitName = "LivrariaPU")
    private EntityManager em;
    
    @EJB
    private ContadorLocal contador;
           
    @Override
    public boolean cadastrar(Livro livro) {
        if (em.contains(livro)) {
            return false;
        }
        em.persist(livro);
        return true;
    }

    @Override
    public boolean atualizar(Livro livro) {
        Livro l = em.find(Livro.class, livro.getCodigo());
        if (l == null) {
            return false;
        } else {
            l = livro;
            em.merge(l);
            return true;
        }
    }

    @Override
    public List<Livro> consultar(HashMap<String, Object> params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Livro> cq = cb.createQuery(Livro.class);
        Root<Livro> root = cq.from(Livro.class);
        cq.select(root);
        List<Predicate> predicates = new ArrayList<>();
        
        if (params.containsKey("titulo")) {
            predicates.add(cb.like(cb.upper((Expression) root.get("titulo")), "%"+params.get("titulo").toString().toUpperCase()+"%"));
        }
        if (params.containsKey("autor")) {
            predicates.add(cb.like(cb.upper((Expression) root.get("autor")), "%"+params.get("autor").toString().toUpperCase()+"%"));
        }
        if (params.containsKey("isbn")) {
            predicates.add(cb.equal((Expression) root.get("isbn"), params.get("isbn").toString()));
        }    
        cq.where(predicates.toArray(new Predicate[]{}));
        TypedQuery<Livro> query = em.createQuery(cq);
        contador.incrementar(); //Incrementa contador de buscas
        return query.getResultList();
    }

    @Override
    public Livro excluir(Long codigo) {
        Livro livro = em.find(Livro.class, codigo);
        if (livro != null) {
            em.remove(livro);
            return livro;
        } else {
            return null;
        }
    }

    @Override
    public Integer numeroDeBuscas() {
        return contador.getCont();
    }

    
}
