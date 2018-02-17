package br.com.api.controller;

import javax.inject.Inject;

import br.com.api.auth.Restricao;
import br.com.api.enumeration.TipoRestricao;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;

@Controller
public class IndexController {

    @Inject private Result result;

    @Get("/")
    @Restricao(tipoRestricao=TipoRestricao.ABERTO)
    public void index() {
        result.include("mensagem", "vraptor-blank-project-rest :)~");
    }
}
