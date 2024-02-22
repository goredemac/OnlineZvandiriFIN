package utils; 
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class FrmPopUpInfo extends JDialog {

        public boolean isCancel = false;
        private String trackHeader = "Pop Up is:";
        private final String message;

        public FrmPopUpInfo(String message) {
            this.message = message;
            initComponents();
        }

    public FrmPopUpInfo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
        private void initComponents() {
    Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();// size of the screen
    Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());// height of the task bar
    setLocation(scrSize.width - 275, scrSize.height - toolHeight.bottom - 120);
    ImageIcon image;
    setSize(225,120);
    setLayout(null);
    setUndecorated(true);
    setLayout(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.weightx = 1.0f;
    constraints.weighty = 1.0f;
    constraints.insets = new Insets(5, 5, 5, 5);
    constraints.fill = GridBagConstraints.BOTH;
    JLabel headingLabel = new JLabel(trackHeader + message);
    image = new ImageIcon(Toolkit.getDefaultToolkit().getImage(FrmPopUpInfo.class.getResource("/images/yourImage.jpg")));
    headingLabel .setIcon(image);
    headingLabel.setOpaque(false);
    add(headingLabel, constraints);
    constraints.gridx++;
    constraints.weightx = 0f;
    constraints.weighty = 0f;
    constraints.fill = GridBagConstraints.NONE;
    constraints.anchor = GridBagConstraints.NORTH;
    JButton cloesButton = new JButton(new AbstractAction("x") {
        @Override
        public void actionPerformed(final ActionEvent e) {
               dispose();
        }
    });
    cloesButton.setMargin(new Insets(1, 4, 1, 4));
    cloesButton.setFocusable(false);
    add(cloesButton, constraints);
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    setVisible(true);
    setAlwaysOnTop(true);
    new Thread(){
          @Override
          public void run() {
               try {
                      Thread.sleep(5000); // time after which pop up will be disappeared.
                      dispose();
               } catch (InterruptedException e) {
                      e.printStackTrace();
               }
          };
    }.start();
}
}