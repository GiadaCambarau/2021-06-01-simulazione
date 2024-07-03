package it.polito.tdp.genes.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulator {
	//parametri ingresso 
	private int nIngegneri;
	private Graph<Genes, DefaultWeightedEdge> grafo;
	private Genes partenza;
	
	//parametri uscita
	private Map<Genes, Integer> mappa;
	private int mesi;
	private Map<Genes, Integer> progresso;
	
	private PriorityQueue<Event> queue;

	public Simulator(Graph<Genes, DefaultWeightedEdge> grafo) {
		super();
		this.grafo = grafo;
	}
	
	public void initialize(Genes g1, int n) {
		this.nIngegneri =n;
		this.partenza = g1;
		this.mesi =0;
		this.mappa = new HashMap<>();
		//inizializza mappa
		//per ogni gene il numero di scienziati
		for (Genes g: this.grafo.vertexSet()) {
			mappa.put(g, 0);
		}
		this.progresso = new HashMap<>();
		mappa.put(g1, n);
		this.queue = new PriorityQueue<>();
		
		for (int i=0; i<n ;i++) {
			queue.add(new Event(1, g1, i));
			
		}
	}
	public void run() {
		while (!this.queue.isEmpty()) {
			Event e = queue.poll();
			this.mesi = e.getTempo();
			if (mesi< (12*3)) {
				processa(e);
			}
		}
	}

	private void processa(Event e) {
		int tempo = e.getTempo();
		Genes corrente = e.getG();
		int ric = e.getnIng();
		
		double random = Math.random();
		//continuo a studiare lo stesso gene
		if (random<0.30) {
			queue.add(new Event(tempo+1, corrente, mappa.get(corrente)));
		}else {
			//devo capire quale tra i vicini devo scegliere
			List<Genes> vicini = Graphs.neighborListOf(this.grafo, corrente);
			Genes nuovo = scegliGene(vicini, corrente);
			int n = mappa.get(nuovo)+1;
			queue.add(new Event(tempo+1, nuovo, n));
			//uno scienziato in meno sta studiando quel gene
			mappa.put(corrente, mappa.get(corrente)-1);
			mappa.put(nuovo, n);
		}
		
		for (Genes g: mappa.keySet()) {
			if (mappa.get(g)>0) {
				this.progresso.put(g, mappa.get(g));
			}
		}
		
	}

	private Genes scegliGene(List<Genes> vicini, Genes corrente) {
		double sommaPesi =0; 
		for (Genes g: vicini) {
			DefaultWeightedEdge d = this.grafo.getEdge(corrente, g);
			double peso = this.grafo.getEdgeWeight(d);
			sommaPesi+= peso;
		}
		Map<Genes, Double> map = new HashMap<>();
		for (Genes g: vicini) {
			DefaultWeightedEdge d = this.grafo.getEdge(corrente, g);
			double peso = this.grafo.getEdgeWeight(d);
			double p = peso /sommaPesi;
			map.put(g, p);
		}
		Genes nuovo = null;
		//ora in una mappa, per ogni gene ho la probabilita che venga scelto
		double r = Math.random();
		double agg =0;
		for (Map.Entry<Genes, Double> a: map.entrySet()) {
			agg+= a.getValue();
			if (r<agg) {
				nuovo = a.getKey();
			}
			
		}
		return nuovo;
	}
	public Map<Genes,Integer> getResult(){
		return mappa;
	}
	
	
	
	

}
