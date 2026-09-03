

public class Ingrediente {
    private int id;
    private String nome;
    private String categoria;
    private String unidade;
    private double quantidade;
    private double estoqueMinimo;

    public Ingrediente (String nome, String categoria, String unidade, double quantidade, double estoqueMinimo){
        this.nome = nome;
        this.categoria = categoria;
        this.unidade = unidade; 
        this.quantidade = quantidade;
        this.estoqueMinimo = estoqueMinimo;
    }

    public int getId() {
        return id;}

    public void setId(int id) {
        this.id = id;}

    public String getNome() {
        return nome;}
    public void setNome(String nome) {
        this.nome = nome; }

    public String getCategoria() {
        return categoria;}
    public void setCategoria(String categoria) {
        this.categoria = categoria;}

    public String getUnidade() {
        return unidade;}
    public void setUnidade(String unidade) {
        this.unidade = unidade;}

    public double getQuantidade() {
        return quantidade;}
    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;}
    
    public double getEstoqueMinimo() {
         return estoqueMinimo;}
    public void setEstoqueMinimo(double estoqueMinimo) {
         this.estoqueMinimo = estoqueMinimo;}

    public boolean precisaReposicao() {
        return quantidade <= estoqueMinimo;}
}
