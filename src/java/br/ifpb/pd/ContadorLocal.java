/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ifpb.pd;

import javax.ejb.Local;

/**
 *
 * @author matheus
 */
@Local
public interface ContadorLocal {
    public void incrementar();
    public Integer getCont();
}
