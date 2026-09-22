import java.awt.*;
import javax.swing.*;
import util.Navegador;
import view.CardapioView;
import view.ClientesView;
import view.EstoqueView;
import view.FuncionarioView;
import view.Inicial;
import view.Login;
import view.MenuPrincipal;
import view.NovoPedidoView;
import view.PedidosView;
import view.ReceitaView;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("La Sottam  Pizzaria");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        CardLayout cardLayout = new CardLayout();
        JPanel painelPrincipal = new JPanel(cardLayout);

        // Gerenciador simplificado
        Navegador navegador = new Navegador(cardLayout, painelPrincipal);

        // Telas desacopladas recebendo apenas comandos de ação
        Inicial telaInicial = new Inicial(e -> navegador.irPara("login"));

        Login telaLogin = new Login(
                () -> navegador.irPara("inicial"), // Ação do botão voltar
                () -> navegador.irPara("menu") // Ação de sucesso no login
        );

        MenuPrincipal telaMenu = new MenuPrincipal(opcao -> {
            if ("SAIR".equals(opcao)) {
                navegador.irPara("login");
            } else if (opcao.equals("CLIENTES")) {
                navegador.irPara("clientes");
            } else if (opcao.equals("FUNCIONÁRIOS")) {
                navegador.irPara("funcionarios");
            } else if (opcao.equals("ESTOQUE")) {
                navegador.irPara("estoque");
            } else if (opcao.equals("CARDÁPIO")) {
                navegador.irPara("cardapio");
            } else if (opcao.equals("PEDIDOS")) {
                navegador.irPara("pedidos");
            } else if (opcao.equals("NOVO PEDIDO")) {
                navegador.irPara("novoPedido");
            } else if (opcao.equals("RECEITAS")) {
                navegador.irPara("receitas");
            }
        });

        CardapioView telaCardapio = new CardapioView(() -> navegador.irPara("menu"));
        ReceitaView telaReceita = new ReceitaView(() -> navegador.irPara("menu"));
        EstoqueView telaEstoque = new EstoqueView(() -> navegador.irPara("menu"));
        ClientesView telaClientes = new ClientesView(() -> navegador.irPara("menu"));
        FuncionarioView telaFuncionarios = new FuncionarioView(() -> navegador.irPara("menu"));
        PedidosView telaPedidos = new PedidosView(() -> navegador.irPara("menu"));
        NovoPedidoView telaNovoPedido = new NovoPedidoView(() -> navegador.irPara("menu"));
        
        painelPrincipal.add(telaInicial, "inicial");
        painelPrincipal.add(telaLogin, "login");
        painelPrincipal.add(telaMenu, "menu");
        painelPrincipal.add(telaCardapio, "cardapio");
        painelPrincipal.add(telaReceita, "receitas");
        painelPrincipal.add(telaEstoque, "estoque");
        painelPrincipal.add(telaClientes, "clientes");
        painelPrincipal.add(telaFuncionarios, "funcionarios");
        
        painelPrincipal.add(telaPedidos, "pedidos");
        painelPrincipal.add(telaNovoPedido, "novoPedido");
        
        frame.setContentPane(painelPrincipal);
        navegador.irPara("inicial");

        frame.setVisible(true);
    }
}