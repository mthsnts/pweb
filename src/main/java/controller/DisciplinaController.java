package controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Contato;
import model.Disciplina;
import util.HibernateUtil;

@ViewScoped
@ManagedBean(name="disciplinaController")
public class DisciplinaController {
	Disciplina disciplina;
	private List<Disciplina> disciplinas = new ArrayList<Disciplina>();
	
	

	@PostConstruct
	public void initialize() {
		disciplina = new Disciplina();
		listarDisciplinas();
	}
	
	public void editar(ActionEvent event) {
		setDisciplina((Disciplina) event.getComponent().getAttributes().get("discilinaSelecionada"));
	}
	
	public void excluir(ActionEvent event) {
		Disciplina disciplina= (Disciplina) event.getComponent().getAttributes().get("disciplinaExcluida");
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction t = null;
		try {
			t = sessao.beginTransaction();
			sessao.delete(disciplina);
			t.commit();
			disciplina= new Disciplina();
		} catch (Exception e) {
			if (t != null) {
				t.rollback();
			}
			throw (e);
		} finally {
			sessao.close();
		}

	}
	
	public void salvar() {

		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction t = null;
		try{
			t = sessao.beginTransaction();
			sessao.merge(disciplina);
			t.commit();
			disciplina = new Disciplina();
		}catch(Exception e){
			if(t != null){
				t.rollback();
			}throw(e);
		
		}finally{
			sessao.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void listarDisciplinas() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();

		try {
			Criteria consulta = sessao.createCriteria(Disciplina.class);
			setDisciplinas(consulta.list());
		} catch (Exception e) {

		} finally {
			sessao.close();
		}

	}


	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public List<Disciplina> getDisciplinas() {
		return disciplinas;
	}

	public void setDisciplinas(List<Disciplina> disciplinas) {
		this.disciplinas = disciplinas;
	}
	
	
	

}
