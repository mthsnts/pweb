package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Contato;
import util.HibernateUtil;

@ViewScoped
@ManagedBean(name = "contatoController")
public class ContatoController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Contato contato;
	private List<Contato> contatos = new ArrayList<>();
	
	@PostConstruct
	public void initialize() {
		 contato = new Contato();
	}
	
	public void salvar() {

		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction t = null;
		try{
			t = sessao.beginTransaction();
			sessao.merge(contato);
			t.commit();
			contato = new Contato();
		}catch(Exception e){
			if(t != null){
				t.rollback();
			}throw(e);
		
		}finally{
			sessao.close();
		}
	}
	
	public void editar(ActionEvent event) {
		setContato((Contato) event.getComponent().getAttributes().get("contatoSelecionado"));
	}

	public void excluir(ActionEvent event) {
		contatos.remove((Contato) event.getComponent().getAttributes().get("contatoExcluido"));
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public List<Contato> getContatos() {
		return contatos;
	}

	public void setContatos(List<Contato> contatos) {
		this.contatos = contatos;
	}

	
}
