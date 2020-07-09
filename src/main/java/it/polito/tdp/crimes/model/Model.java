package it.polito.tdp.crimes.model;

import java.util.List;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private EventsDao dao;
	
	
	public Model() {
		dao=new EventsDao();
	}
	public void creaGrafo(int mese, Event e) {
		
	}
	public List<Integer> getMesi(){
		return dao.getMeseCrimini();
	}
	public List<String> getTipo(){
		return dao.getCategoriaReato();
	}
}
