package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	private GenesDao dao;
	private List<Genes> geni;
	private Graph<Genes, DefaultWeightedEdge> grafo;
	private Map<String, Genes> mappa;
	
	public Model() {
		this.dao = new GenesDao();
		this.geni = new ArrayList<>();
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.mappa = new HashMap<>();
	}
	
	public void creaGrafo() {
		for (Genes g: dao.getAllGenes()) {
			mappa.put(g.getGeneId(), g);
		}
		Graphs.addAllVertices(this.grafo, dao.getEssential(mappa));
		List <Arco> archi = dao.getArchi(mappa);
		for (Arco a: archi) {
			if (a.getG1().getChromosome() == a.getG2().getChromosome()) {
				double peso = (Math.abs(a.getPeso()))*2;
				Graphs.addEdgeWithVertices(this.grafo, a.getG1(), a.getG2(), peso);
				a.setPeso(peso);
			}else if(a.getG1().getChromosome() != a.getG2().getChromosome()) {
				double peso = Math.abs(a.getPeso());
				a.setPeso(peso);
				Graphs.addEdgeWithVertices(this.grafo, a.getG1(), a.getG2(), peso);
			}
		}
		mappa.clear();
		
		for (Genes g: this.grafo.vertexSet()) {
			mappa.put(g.getGeneId(), g);
		}
	}
	
	public int getV() {
		return this.grafo.vertexSet().size();
		
	}
	public int getA() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Arco> getAdj(Genes g1){
		List<Genes> vicini = Graphs.neighborListOf(this.grafo, g1);
		List<Arco> result = new ArrayList<>();
		for (Genes g : vicini) {
			DefaultWeightedEdge e = this.grafo.getEdge(g1, g);
			double peso = grafo.getEdgeWeight(e);
			Arco a = new Arco (g1, g, peso);
			result.add(a);
		}
		return result;
	}
	
	public Set<Genes> fetVertici(){
		return this.grafo.vertexSet();
	}
	
	public Map<Genes, Integer> simula(int n, Genes g){
		Simulator sim = new Simulator(grafo);
		sim.initialize(g, n);
		sim.run();
		Map<Genes, Integer> result =sim.getResult();
		return result;
	}
	
	
	
	
	
	
	
}
