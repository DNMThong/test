package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class a01 extends JFrame implements Runnable, ThreadFactory {

	private JPanel contentPane;
	private WebcamPanel panel = null;
    private Webcam webcam = null;
	private JPanel panel_1;
	private Executor executor = Executors.newSingleThreadExecutor(this);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					a01 frame = new a01();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public a01() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel_1 = new JPanel();
		panel_1.setBounds(10, 11, 468, 393);
		contentPane.add(panel_1);
		initWebcam();
	}
	
	public void initWebcam() {
		Dimension size = WebcamResolution.QVGA.getSize();
        webcam = Webcam.getWebcams().get(0); //0 is default webcam
        webcam.setViewSize(size);

        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size);
        panel.setFPSDisplayed(true);

        panel_1.add(panel);
       
//        jPanel2.add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 470, 300));
//
        executor.execute(this);
	}

	 @Override
	    public void run() {
	        do {
	            try {
	                Thread.sleep(100);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }

	            Result result = null;
	            BufferedImage image = null;

	            if (webcam.isOpen()) {
	                if ((image = webcam.getImage()) == null) {
	                    continue;
	                }
	            }

	            LuminanceSource source = new BufferedImageLuminanceSource(image);
	            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

	            try {
	                result = new MultiFormatReader().decode(bitmap);
	            } catch (NotFoundException e) {
	                //No result...
	            }

	            if (result != null) {
	            	JOptionPane.showMessageDialog(contentPane, result.getText());
	            }
	        } while (true);
	    }

	    @Override
	    public Thread newThread(Runnable r) {
	        Thread t = new Thread(r, "My Thread");
	        t.setDaemon(true);
	        return t;
	    }
}
