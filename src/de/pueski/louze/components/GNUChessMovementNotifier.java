package de.pueski.louze.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import de.pueski.louze.app.Louze;

/**
 * @author Matthias Pueski (08.07.2010)
 *
 */

public class GNUChessMovementNotifier implements MovementNotifier {
	
	private Process p;
	
	public GNUChessMovementNotifier() {

		
		Runnable r = new Runnable() {
			
			public void run() {

				
				String[] commands = new String[3];
				
		
				commands[0] = "bash";
				commands[1] = "-c";
				commands[2] = "gnuchess -x";						
				
				String line = null;
				
				try {
					p = Runtime.getRuntime().exec(commands);
					
					if (p == null) {
						return;
					}						
										
					InputStream is = p.getInputStream();

					BufferedReader br = new BufferedReader(new InputStreamReader(is));
										
					while ((line = br.readLine()) != null) {
						
						if (line.startsWith("My move is")) {
							
							String move = line.substring(line.lastIndexOf(":")+2);
							Louze.getInstance().getModel().makeMove(move);
							Louze.getInstance().getChessPanel().invalidate();
							Louze.getInstance().getChessPanel().repaint();
						}
						else {
							System.out.println(line);
						}
						
					}
					
					br.close();

				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		Thread t = new Thread(r);
		t.start();
		
	}
	
	@Override
	public void moved(String move) {
			
		if (p == null) {
			return;
		}

		OutputStream os = p.getOutputStream();
		
		try {
			os.write(new String(move+"\n").getBytes());
			os.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}					
		
	}

	@Override
	public String opponentMoved() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
