package it.polito.tdp.genes.model;

import java.util.Objects;

public class Event implements Comparable<Event> {
	
	private int tempo;
	private Genes g;
	private int nIng;
	
	public Event(int tempo, Genes g, int nIng) {
		super();
		this.tempo = tempo;
		this.g = g;
		this.nIng = nIng;
	}

	public int getTempo() {
		return tempo;
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}

	public Genes getG() {
		return g;
	}

	public void setG(Genes g) {
		this.g = g;
	}

	public int getnIng() {
		return nIng;
	}

	public void setnIng(int nIng) {
		this.nIng = nIng;
	}

	@Override
	public int hashCode() {
		return Objects.hash(g, nIng, tempo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		return Objects.equals(g, other.g) && nIng == other.nIng && tempo == other.tempo;
	}

	@Override
	public int compareTo(Event o) {
		// TODO Auto-generated method stub
		return this.tempo-o.tempo;
	}
	
	
	
	
	
	
	

}
