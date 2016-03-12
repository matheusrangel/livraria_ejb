/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ifpb.pd.sb;

import br.ifpb.pd.interfaces.ContadorLocal;
import br.ifpb.pd.interfaces.GerenciadorRemote;
import br.ifpb.pd.interfaces.GerenciadorWS;
import br.ifpb.pd.interfaces.LogLocal;
import br.ifpb.pd.model.Acao;
import br.ifpb.pd.model.Livro;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
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
@WebService(serviceName = "livraria", endpointInterface = "br.ifpb.pd.interfaces.GerenciadorWS")
public class GerenciadorSB implements GerenciadorRemote, GerenciadorWS {
    
    @PersistenceContext(unitName = "LivrariaPU")
    private EntityManager em;
    
    @EJB
    private ContadorLocal contador;
    
    @EJB
    private LogLocal log;
           
    @Override
    public boolean cadastrar(Livro livro) {
        if (em.contains(livro)) {
            return false;
        }
        em.persist(livro);
        log.novaAcao(new Acao("CADASTRO do livro "+livro.toString(), new Date()));
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
            log.novaAcao(new Acao("ATUALIZAÇÃO do livro "+livro.toString(), new Date()));
            return true;
        }
    }
    
    @Override
    public List<Livro> consultar(String titulo, String autor, String isbn) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Livro> cq = cb.createQuery(Livro.class);
        Root<Livro> root = cq.from(Livro.class);
        cq.select(root);
        List<Predicate> predicates = new ArrayList<>();
        
        if (titulo != null) {
            predicates.add(cb.like(cb.upper((Expression) root.get("titulo")), "%"+titulo.toUpperCase()+"%"));
        }
        if (autor != null) {
            predicates.add(cb.like(cb.upper((Expression) root.get("autor")), "%"+autor.toUpperCase()+"%"));
        }
        if (isbn != null) {
            predicates.add(cb.equal((Expression) root.get("isbn"), isbn));
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
            log.novaAcao(new Acao("EXCLUSÃO do livro "+livro.toString(), new Date()));
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
