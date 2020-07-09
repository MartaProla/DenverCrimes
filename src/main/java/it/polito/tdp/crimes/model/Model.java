package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private EventsDao dao;
	private SimpleWeightedGraph<String, DefaultWeightedEdge> grafo;
	private List<String>best;
	
	
	public Model() {
		dao=new EventsDao();
	}
	public void creaGrafo(int mese, String evento) {
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		List<Adiacenza> adiacenze=this.dao.getAdiacenze(mese, evento);
		for(Adiacenza a: adiacenze) {
			//vertici 
			if(!this.grafo.containsVertex(a.getV1()))
				this.grafo.addVertex(a.getV1());
			if(!this.grafo.containsVertex(a.getV2()))
				this.grafo.addVertex(a.getV2());
			
			if(this.grafo.getEdge(a.getV1(), a.getV2())==null) {
				Graphs.addEdgeWithVertices(this.grafo, a.getV1(), a.getV2(), a.getPeso());
			}
		}
		System.out.println(String.format("Grafo creato con %d vertici e %d archi", this.grafo.vertexSet().size(),this.grafo.edgeSet().size()));
		
	}
	public List<Integer> getMesi(){
		return dao.getMeseCrimini();
	}
	public List<String> getTipo(){
		return dao.getCategoriaReato();
	}
	
	public List<Arco>pesoMaggiorePesoMedioGrafo(){
		//calcola il peso medio degli archi
		double pesoMedio=0.0;
		for(DefaultWeightedEdge e:this.grafo.edgeSet())
			pesoMedio+=this.grafo.getEdgeWeight(e);
		pesoMedio=pesoMedio/this.grafo.edgeSet().size();
		List<Arco>archi=new ArrayList<>();
		for(DefaultWeightedEdge e:this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e)>pesoMedio) {
				archi.add(new Arco(this.grafo.getEdgeSource(e),this.grafo.getEdgeTarget(e),this.grafo.getEdgeWeight(e)));
			}
		}
		Collections.sort(archi);
		return archi;
	}
	
	public List<String>trovaPercorso(String sorgente, String destinazione){
		List<String>parziale=new ArrayList<>();
		this.best=new ArrayList<>();
		parziale.add(sorgente);
		trovaRicorsione(destinazione, parziale, 0);
		return this.best;
	}
	private void trovaRicorsione(String destinazione, List<String> parziale, int L) {
		//caso terminale --> QUANDO L'UTLIMO VERTICE INSERITO IN PARZIALE COINCIDE CON LA DESTINAZIONE
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
			if(parziale.size()>this.best.size()) {
				this.best=new ArrayList<>(parziale);
			}
			return;
		}
		//scorro i vicini dell'ultimo vertice
		for(String vicino: Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			//controllo che il vertice sia in parziale
			if(!parziale.contains(vicino)) {
				//provo ad aggiungere 
				parziale.add(vicino);
				this.trovaRicorsione(destinazione, parziale, L+1);
				//backtracking
				parziale.remove(parziale.size()-1);
			}
		}
	}
	
}
