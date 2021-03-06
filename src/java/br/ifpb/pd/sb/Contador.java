/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ifpb.pd.sb;

import br.ifpb.pd.interfaces.ContadorLocal;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

/**
 *
 * @author matheus
 */
@Singleton
public class Contador implements ContadorLocal {
    
    private Integer cont;

    public Contador() {
        cont = 0;
    }
    
    @Lock(LockType.WRITE)
    @Override
    public void incrementar() {
        this.cont++;
    }
    
    @Lock(LockType.READ)
    @Override
    public Integer getCont() {
        return cont;
    }
    
}
