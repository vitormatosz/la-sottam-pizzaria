import javax.swing.*;

import util.Navegador;

import java.awt.*;

import view.Inicial;
import view.Login;
import view.MenuPrincipal;
import view.ClientesView;
import view.FuncionarioView;

public class Main {
    public static void main(String[] args) {
            JFrame frame = new JFrame("La Sottam Pizzaria");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            CardLayout cardLayout = new CardLayout();
            JPanel painelPrincipal = new JPanel(cardLayout);
            
            // Gerenciador simplificado
            Navegador navegador = new Navegador(cardLayout, painelPrincipal);

            // Telas desacopladas recebendo apenas comandos de ação
            Inicial telaInicial = new Inicial(e -> navegador.irPara("menu"));
            
            Login telaLogin = new Login(
                () -> navegador.irPara("inicial"), // Ação do botão voltar
                () -> navegador.irPara("menu")     // Ação de sucesso no login
            );

            MenuPrincipal telaMenu = new MenuPrincipal(opcao -> {
                if ("SAIR".equals(opcao)) {
                    navegador.irPara("login");
                } else if(opcao.equals("CLIENTES")) {
                    navegador.irPara("clientes");
                } else if (opcao.equals("FUNCIONÁRIOS")) {
                    navegador.irPara("funcionarios");
                }
            });

            ClientesView telaClientes = new ClientesView(() -> navegador.irPara("menu"));
            FuncionarioView telaFuncionarios = new FuncionarioView(() -> navegador.irPara("menu"));

            painelPrincipal.add(telaInicial, "inicial");
            painelPrincipal.add(telaLogin, "login");
            painelPrincipal.add(telaMenu, "menu");
            painelPrincipal.add(telaClientes, "clientes");
            painelPrincipal.add(telaFuncionarios, "funcionarios");

            frame.setContentPane(painelPrincipal);
            navegador.irPara("inicial");

            frame.setVisible(true);
    }
}