/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ifpb.pd.interfaces;

import br.ifpb.pd.model.Livro;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 *
 * @author matheus
 */
@WebService(targetNamespace = "http://sb.pd.ifpb.br/")
public interface GerenciadorWS {
    @WebMethod(operationName = "consultar")
    @WebResult(name = "livro")
    public List<Livro> consultar(
            @WebParam(name = "titulo") String titulo,
            @WebParam(name = "autor") String autor, 
            @WebParam(name = "isbn") String isbn);
}
