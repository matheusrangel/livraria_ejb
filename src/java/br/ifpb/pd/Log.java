/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ifpb.pd;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author matheus
 */
@Stateless
public class Log implements LogLocal {
    
    @PersistenceContext(unitName = "LivrariaPU")
    private EntityManager em;

    @Override
    public void novaAcao(Acao acao) {
        em.persist(acao);
    }

    
}
