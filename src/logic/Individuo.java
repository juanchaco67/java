package logic;

public class Individuo {
	private int cromosoma[][];
	private int fitnes;
	public static final int ROW=9;
	public static final int COL=12;
	private boolean cromosomaActivo[][];
	private boolean cromosomaActivoEstaticas[][];
	private static final  int MODELO[]={1,2,3,4,5,6,7,8,9};
	public Individuo(int cromosoma[][],boolean cromosomaActivo[][]) {
		// TODO Auto-generated constructor stub
		setCromosoma(cromosoma);
		setCromosomaActivo(cromosomaActivo);


	}
	public Individuo() {
		// TODO Auto-generated constructor stub
		setCromosoma(new int [ROW][COL]);
		setCromosomaActivo(new boolean[ROW][ROW]);
		setCromosomaActivoEstaticas(new boolean[ROW][COL]);

	}
	public void mostrarCromosoma(){
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				System.out.print(cromosoma[i][j]+",");
			}
			System.out.println(":fitnes: "+fitnes);
		}
	}
	public void mostrarCromosomaActivo(){
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL-3; j++) {
				if(cromosomaActivo[i][j])
					System.out.print(1+",");
				else
					System.out.print(0+",");

			}
			System.out.println();
		}
	}
	public void mostrarCromosomaActivoEstatico(){
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL-3; j++) {
				if(cromosomaActivoEstaticas[i][j])
					System.out.print(1+",");
				else
					System.out.print(0+",");

			}
			System.out.println();
		}
	}
	private void contarFila(){
		int contador=0;
		for (int i = 0; i < ROW; i++) {
			for (int j2 = 0; j2 < MODELO.length; j2++) {
				for (int j = 0; j < COL; j++) {
					if(cromosoma[i][j]==MODELO[j2])
					{
						contador++;
						break;
					}
				}	
			}
			cromosoma[i][9]=contador;
			contador=0;
		}	
	}
	private void contarColumna(){
		int contador=0;
		for (int i = 0; i < ROW; i++) {
			for (int j2 = 0; j2 < MODELO.length; j2++) {
				for (int j = 0; j < COL-3; j++) {
					if(cromosoma[j][i]==MODELO[j2])
					{
						contador++;
						break;
					}
				}	
			}
			cromosoma[i][10]=contador;
			contador=0;
		}	
	}
	private void contarArea(int row,int col,int k){
		int aux=0;
		for (int j2 = 0; j2 < MODELO.length; j2++) {
			bucle:
				for (int i = 0; i <3; i++) {
					for (int j = 0; j < 3; j++) {				
						if(cromosoma[i+row][j+col]==MODELO[j2]){
							aux++;
							break bucle;
						}
					}				
				}

		}			
		cromosoma[k][11]=aux;

	}
	private void contarCuadrante(){
		int k=0;	
		for (int j = 0; j < 9; j+=3) {			
			for (int i = 0; i < 9; i+=3,k++) {				
				contarArea(j,i,k);
			}
		}
	}
	public void calcularFitnes(){
		contarFila();
		contarColumna();
		contarCuadrante();	
		int fitnes=0;

		for (int i = 9; i < COL; i++) {
			for (int j = 0; j < ROW; j++) {
				fitnes+=cromosoma[j][i];
			}
		}

		this.fitnes=fitnes;
	}
	public boolean estafila(int row,int posCol,int dato){
		for (int i = 0; i < COL-3; i++) {
			if((cromosoma[row][i]==dato) && (posCol!=i))
				return true;
		}
		return false;
	}
	public boolean estaColumna(int col,int posRow,int dato){
		for (int i = 0; i < ROW; i++) {
			if((cromosoma[i][col]==dato) && (posRow!=i))
				return true;
		}
		return false;
	}
	public int []elegirCuadrante(int row ,int col){
		int []cuadrante=new int [2];
		if(row>=0 && row<=2){
			if(col>=0 && col<=2){
				row=0;
				col=0;
			}
			if(col>=3 && col<=5){
				row=0;
				col=3;
			}
			if(col>=6 && col<=8){
				row=0;
				col=6;
			}			
		}		
		if(row>=3 && row<=5){
			if(col>=0 && col<=2){
				row=3;
				col=0;
			}
			if(col>=3 && col<=5){
				row=3;
				col=3;
			}
			if(col>=6 && col<=8){
				row=3;
				col=6;
			}			
		}
		if(row>=6 && row<=8){
			if(col>=0 && col<=2){
				row=6;
				col=0;
			}
			if(col>=3 && col<=5){
				row=6;
				col=3;
			}
			if(col>=6 && col<=8){
				row=6;
				col=6;
			}			

		}	
		cuadrante[0]=row;
		cuadrante[1]=col;
		return  cuadrante;
		
	}
	public boolean estaCuadrante(int row,int col,int posRow,int posCol,int dato){
		int cuadrante[]=elegirCuadrante(row, col);
		row=cuadrante[0];
		col=cuadrante[1];
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if(cromosoma[i+row][j+col]==dato && (i+row)!=posRow && (j+col) !=posCol)
					return true;
			}
		}			 
		return false;
	}
	public void coincidencias(){

		for (int i = 0; i < ROW; i++) {		
			for (int j = 0; j < COL-3; j++) {
				if(!cromosomaActivoEstaticas[i][j]){
					if(estaArea(i, j,cromosoma[i][j]))
						cromosomaActivo[i][j]=true;
					else
						cromosomaActivo[i][j]=false;
				}
			}
		}
	}
	public boolean estaArea(int i,int j,int dato){
		int aux=0;
		if(!estafila(i, j, dato))
			aux++;
		if(!estaColumna(j,i,dato))
			aux++;
		if(!estaCuadrante(i,j,i,j,dato))
			aux++;
		if(aux==3)
			return true;
		else
			return false;
	}
	public int cantidadCromosomaActivo(){
		int aux=0;
		for (int i = 0; i < MODELO.length; i++) {
			for (int j = 0; j < MODELO.length; j++) {
				if(cromosomaActivo[i][j])
					aux++;
			}
		}
		return aux;
	}




	public void add(int row,int col,int dato){
		cromosoma[row][col]=dato;
	}
	public void addActivo(int row,int col,boolean dato){

		cromosomaActivo[row][col]=dato;
	}
	public void addActivoEstatico(int row,int col,boolean dato){

		cromosomaActivoEstaticas[row][col]=dato;
	}
	public int[][] getCromosoma() {
		return cromosoma;
	}
	public void setCromosoma(int[][] cromosoma) {
		this.cromosoma = cromosoma;
	}
	public int getFitnes() {
		return fitnes;
	}
	public void setFitnes(int fitnes) {
		this.fitnes = fitnes;
	}
	public boolean[][] getCromosomaActivo() {
		return cromosomaActivo;
	}
	public void setCromosomaActivo(boolean[][] cromosomaActivo) {
		this.cromosomaActivo = cromosomaActivo;
	}
	public boolean[][] getCromosomaActivoEstaticas() {
		return cromosomaActivoEstaticas;
	}
	public void setCromosomaActivoEstaticas(boolean[][] cromosomaActivoEstaticas) {
		this.cromosomaActivoEstaticas = cromosomaActivoEstaticas;
	}	


}