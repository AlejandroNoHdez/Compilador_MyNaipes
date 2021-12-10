import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import analizador.Analizador;
import analizadorLexico.AnalizadorLexico;

public class ClaseFrame extends JFrame implements ActionListener
	{
		private Container contenedor;
		JLabel labelTitulo;
		JTextArea areaDeTexto;
		JTextArea areaDeResultado;
		JButton botonAbrir;
		JButton botonGuardar;
		JScrollPane scrollPaneArea;
		JScrollPane Area;
		JFileChooser fileChooser;
		String texto;
		String archivo;
		
		public ClaseFrame()
		{
			contenedor=getContentPane();
			contenedor.setLayout(null);
			
			fileChooser=new JFileChooser();
			
			labelTitulo= new JLabel();
			labelTitulo.setText("COMPILADOR MY NAIPES");
			labelTitulo.setBounds(110, 20, 180, 23);
			
			areaDeTexto = new JTextArea();

			areaDeTexto.setLineWrap(true);

			areaDeTexto.setWrapStyleWord(true);
		   	scrollPaneArea = new JScrollPane();
			scrollPaneArea.setBounds(20, 50, 350, 270);
			scrollPaneArea.setViewportView(areaDeTexto);

			areaDeResultado = new JTextArea();

			areaDeResultado.setLineWrap(true);

			areaDeResultado.setWrapStyleWord(true);
			Area = new JScrollPane();
			Area.setBounds(20,400, 350, 270);
			Area.setViewportView(areaDeResultado);
	       	
			botonAbrir= new JButton();
			botonAbrir.setText("Abrir");
			botonAbrir.setBounds(100, 330, 80, 23);
			botonAbrir.addActionListener(this);
			
			botonGuardar= new JButton();
			botonGuardar.setText("Enviar");
			botonGuardar.setBounds(220, 330, 80, 23);
			botonGuardar.addActionListener(this);
			
			contenedor.add(labelTitulo);
			contenedor.add(scrollPaneArea);
			contenedor.add(botonAbrir);
			contenedor.add(botonGuardar);
			contenedor.add(Area);
			setTitle("LENGUAJES Y AUTÓMATAS II");
			setSize(400,730);
			setLocationRelativeTo(null);
			
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

		@Override
		public void actionPerformed(ActionEvent evento) {
			if (evento.getSource()==botonAbrir)
			{
				archivo = abrirArchivo();
				areaDeTexto.setText(archivo);
			}
			
			if (evento.getSource()==botonGuardar)
			{
				mandarDatos();
			}
		}

		private String abrirArchivo() {
					
			String aux=""; 		
	 		texto="";
		
	 		try
			{
	 			
	    		fileChooser.showOpenDialog(this);

	 			File abre=fileChooser.getSelectedFile();

	 			if(abre!=null)
	 			{ 				
	 				FileReader archivos=new FileReader(abre);
	 				BufferedReader lee=new BufferedReader(archivos);
	 				while((aux=lee.readLine())!=null)
	 					{
	 					 texto+= aux+ "\n";
	 					}

	 		  		lee.close();
	 			} 			
	 		}
	 		catch(IOException ex)
			{
			  JOptionPane.showMessageDialog(null,ex+"" +
			  		"\nNo se ha encontrado el archivo",
			  		"ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
			}
				return texto;
		}

		private void mandarDatos() {
	 		try
	 		{
				archivo = areaDeTexto.getText();
				String nuevo = archivo.replace("\n", "");
				System.out.println(nuevo);
				AnalizadorLexico lex = new AnalizadorLexico();
				lex.bandera(nuevo);
				Analizador analizar = new Analizador(lex);
				analizar.programa();
				
	 		 }
	 	   catch(IOException ex)
		   {
			 JOptionPane.showMessageDialog(null,
					 "Su archivo no se ha guardado",
					 "Advertencia",JOptionPane.WARNING_MESSAGE);
		   }
		}

            public static void main(String[] args) {
                  ClaseFrame miVentana = new ClaseFrame();
                  miVentana.setVisible(true);
            }
	}