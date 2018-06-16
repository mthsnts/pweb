package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.apache.poi.ss.formula.functions.T;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Aluno;
import model.Contato;
import util.HibernateUtil;

@ViewScoped
@ManagedBean(name = "alunoController")
public class AlunoController implements Serializable {

	private static final long serialVersionUID = 1L;

	private Aluno aluno;
	private List<Aluno> alunos = new ArrayList<Aluno>();
	private List<Contato> contatos = new ArrayList<Contato>();
	private static EntityManager em;
	private Class<T> clazz;

	@PostConstruct
	public void initialize() {
		aluno = new Aluno();
		listarContatos();
		listarAlunos();
	}

	public T findById(Integer integer) {
		return em.find(clazz, integer);
	}
	
	public void remove(Integer integer) {
		T t = findById(integer);
		try {
			em.getTransaction().begin();
			em.remove(t);
			em.getTransaction().commit();

		} catch (PersistenceException e) {
			e.printStackTrace();
			em.getTransaction().rollback();
		}
}

	public void editar(ActionEvent event) {
		setAluno((Aluno) event.getComponent().getAttributes().get("alunoSelecionado"));
	}

	public void contatoSelecinado(ActionEvent event) {
		aluno.setContato((Contato) event.getComponent().getAttributes().get("conatoSlecionado"));
	}

	public void salvar() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction t = null;
		try {
			t = sessao.beginTransaction();
			sessao.merge(aluno);
			t.commit();
			aluno = new Aluno();
		} catch (Exception e) {
			if (t != null) {
				t.rollback();
			}
			throw (e);

		} finally {
			sessao.close();
		}

	}

	@SuppressWarnings("unchecked")
	public void listarContatos() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();

		try {
			Criteria consulta = sessao.createCriteria(Contato.class);
			setContatos(consulta.list());
		} catch (Exception e) {

		} finally {
			sessao.close();
		}

	}

	@SuppressWarnings("unchecked")
	public void listarAlunos() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();

		try {
			Criteria consulta = sessao.createCriteria(Aluno.class);
			alunos = consulta.list();
		} catch (Exception e) {

		} finally {
			sessao.close();
		}

	}

	public void novoAluno() {
		aluno = new Aluno();
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}

	public List<Contato> getContatos() {
		return contatos;
	}

	public void setContatos(List<Contato> contatos) {
		this.contatos = contatos;
	}
	
	

}
