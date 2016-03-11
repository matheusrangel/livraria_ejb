/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ifpb.pd;

import java.util.HashMap;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author matheus
 */
@Remote
public interface GerenciadorRemote {
    public boolean cadastrar(Livro livro);
    public boolean atualizar(Livro livro);
    public List<Livro> consultar(HashMap<String, Object> params);
    public Livro excluir(Long codigo);
    public Integer numeroDeBuscas();
}
