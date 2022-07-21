package rotp.ui.main;

import java.awt.event.MouseWheelEvent;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import rotp.model.empires.Empire;
import rotp.model.galaxy.StarSystem;
import rotp.model.game.GameSession;
import rotp.model.game.GovernorOptions;
import rotp.ui.RotPUI;

/**
 * Produced using Netbeans Swing GUI builder.
 */
public class GovernorOptionsPanel extends javax.swing.JPanel {
    private final JFrame frame;
    public GovernorOptionsPanel(JFrame frame) {
        this.frame = frame;
        initComponents();
        // initial values
        GovernorOptions options = GameSession.instance().getGovernorOptions();
        this.governorDefault.setSelected(options.isGovernorOnByDefault());
        this.legacyGrowthMode.setSelected(options.legacyGrowthMode());
        this.autotransport.setSelected(options.isAutotransport());
        this.autotransportXilmi.setSelected(options.isAutotransportXilmi());
        this.autoInfiltrate.setSelected(options.isAutoInfiltrate());
        this.autoSpy.setSelected(options.isAutoSpy());
        this.allowUngoverned.setSelected(options.isAutotransportUngoverned());
        this.transportMaxTurns.setValue(options.getTransportMaxTurns());
        this.transportRichDisabled.setSelected(options.isTransportRichDisabled());
        this.transportPoorDouble.setSelected(options.isTransportPoorDouble());
        switch (GameSession.instance().getGovernorOptions().getGates()) {
            case None:
                this.stargateOff.setSelected(true);
                break;
            case Rich:
                this.stargateRich.setSelected(true);
                break;
            case All:
                this.stargateOn.setSelected(true);
                break;
        }
        this.missileBases.setValue(options.getMinimumMissileBases());
        this.shieldWithoutBases.setSelected(options.getShieldWithoutBases());
        this.autospend.setSelected(options.isAutospend());
        this.reserve.setValue(options.getReserve());
        this.shipbuilding.setSelected(options.isShipbuilding());
        this.autoScout.setSelected(options.isAutoScout());
        this.autoColonize.setSelected(options.isAutoColonize());
        this.completionist.setEnabled(isCompletionistEnabled());
        this.autoAttack.setSelected(options.isAutoAttack());
        this.autoScoutShipCount.setValue(options.getAutoScoutShipCount());
        this.autoColonyShipCount.setValue(options.getAutoColonyShipCount());
        this.autoAttackShipCount.setValue(options.getAutoAttackShipCount());
        this.autoApplyToggleButton.setEnabled(options.isAutoApply());
    }

    public boolean isCompletionistEnabled() {
        if (GameSession.instance().galaxy() == null) {
            return false;
        }
        float colonized = GameSession.instance().galaxy().numColonizedSystems() / (float)GameSession.instance().galaxy().numStarSystems();
        float controlled = GameSession.instance().galaxy().player().numColonies() / GameSession.instance().galaxy().numColonizedSystems();
        boolean completed = GameSession.instance().galaxy().player().tech().researchCompleted();
        System.out.format("Colonized %.2f galaxy, controlled %.2f galaxy, completed research %s%n", colonized, controlled, completed);
        if (colonized >= 0.3 && controlled >= 0.5 && completed) {
            return true;
        } else {
            return false;
        }
    }
    public void performCompletionist() {
        // game not in session
        if (GameSession.instance().galaxy() == null) {
            return;
        }
        // Techs to give
        String techs[] = {
                "ImprovedTerraforming:8",
                "SoilEnrichment:1",
                "AtmosphereEnrichment:0",
                "ControlEnvironment:6",
                "Stargate:0"
        };
        for (Empire e: GameSession.instance().galaxy().empires()) {
            if (e.extinct()) {
                continue;
            }
            for (String t: techs) {
                e.tech().allowResearch(t);
            }
        }
    }
    private void applyAction() {// BR: 
    	GovernorOptions options = GameSession.instance().getGovernorOptions();
        options.setGovernorOnByDefault(governorDefault.isSelected());
        options.setLegacyGrowthMode(legacyGrowthMode.isSelected());
        options.setAutotransport(autotransport.isSelected());
        options.setAutotransportXilmi(autotransportXilmi.isSelected());
        options.setAutotransportUngoverned(allowUngoverned.isSelected());
        options.setTransportMaxTurns((Integer)transportMaxTurns.getValue());
        options.setTransportRichDisabled(transportRichDisabled.isSelected());
        options.setTransportPoorDouble(transportPoorDouble.isSelected());
        applyStargates();
        options.setMinimumMissileBases((Integer)missileBases.getValue());
        options.setShieldWithoutBases(shieldWithoutBases.isSelected());
        options.setAutospend(autospend.isSelected());
        options.setAutoInfiltrate(autoInfiltrate.isSelected());
        options.setAutoSpy(autoSpy.isSelected());
        options.setReserve((Integer)reserve.getValue());
        options.setShipbuilding(shipbuilding.isSelected());
        options.setAutoScout(autoScout.isSelected());
        options.setAutoColonize(autoColonize.isSelected());
        options.setAutoAttack(autoAttack.isSelected());
        options.setAutoScoutShipCount((Integer)autoScoutShipCount.getValue());
        options.setAutoColonyShipCount((Integer)autoColonyShipCount.getValue());
        options.setAutoAttackShipCount((Integer)autoAttackShipCount.getValue());
    }                                   
    private void applyStargates() {// BR: 
    	GovernorOptions options = GameSession.instance().getGovernorOptions();
        if (stargateOff.isSelected()) {
            options.setGates(GovernorOptions.GatesGovernor.None);
        } else if (stargateRich.isSelected()) {
            options.setGates(GovernorOptions.GatesGovernor.Rich);
        } else if (stargateOn.isSelected()) {
            options.setGates(GovernorOptions.GatesGovernor.All);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        stargateOptions = new javax.swing.ButtonGroup();
        governorDefault = new javax.swing.JCheckBox();
        javax.swing.JPanel autotransportPanel = new javax.swing.JPanel();
        autotransport = new javax.swing.JCheckBox();
        transportMaxTurns = new javax.swing.JSpinner();
        javax.swing.JLabel transportMaxTurnsLabel = new javax.swing.JLabel();
        javax.swing.JLabel transportMaxTurnsNebula = new javax.swing.JLabel();
        transportRichDisabled = new javax.swing.JCheckBox();
        transportPoorDouble = new javax.swing.JCheckBox();
        autotransportXilmi = new javax.swing.JCheckBox();
        allowUngoverned = new javax.swing.JCheckBox();
        allGovernorsOn = new javax.swing.JButton();
        allGovernorsOff = new javax.swing.JButton();
        javax.swing.JPanel stargatePanel = new javax.swing.JPanel();
        stargateOff = new javax.swing.JRadioButton();
        stargateRich = new javax.swing.JRadioButton();
        stargateOn = new javax.swing.JRadioButton();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        missileBases = new javax.swing.JSpinner();
        missileBasesLabel = new javax.swing.JLabel();
        autospend = new javax.swing.JCheckBox();
        reserve = new javax.swing.JSpinner();
        resrveLabel = new javax.swing.JLabel();
        shipbuilding = new javax.swing.JCheckBox();
        autoScout = new javax.swing.JCheckBox();
        autoColonize = new javax.swing.JCheckBox();
        completionist = new javax.swing.JButton();
        autoAttack = new javax.swing.JCheckBox();
        autoColonyShipCount = new javax.swing.JSpinner();
        autoColonyShipCountLabel = new javax.swing.JLabel();
        autoScoutShipCount = new javax.swing.JSpinner();
        autoAttackShipCount = new javax.swing.JSpinner();
        autoScoutShipCountLabel = new javax.swing.JLabel();
        autoAttackShipCountLabel = new javax.swing.JLabel();
        shieldWithoutBases = new javax.swing.JCheckBox();
        legacyGrowthMode = new javax.swing.JCheckBox();
        autoSpy = new javax.swing.JCheckBox();
        autoInfiltrate = new javax.swing.JCheckBox();
        applyButton = new javax.swing.JButton();
        autoApplyToggleButton = new javax.swing.JToggleButton();

        governorDefault.setText("Governor is on by default");

        autotransportPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Autotransport Options"));

        autotransport.setText("Population automatically transported from colonies at max production capacity");
        autotransport.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                autotransportActionPerformed(evt);
            }
        });

        transportMaxTurns.setModel(new javax.swing.SpinnerNumberModel(15, 1, 15, 1));
        transportMaxTurns.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                transportMaxTurnsStateChanged(evt);
            }
        });
        transportMaxTurns.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                transportMaxTurnsMouseWheelMoved(evt);
            }
        });

        transportMaxTurnsLabel.setText("Maximum transport distance in turns");
        transportMaxTurnsLabel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                transportMaxTurnsLabelMouseWheelMoved(evt);
            }
        });

        transportMaxTurnsNebula.setText("(1.5x higher distance when transporting to nebulae)");

        transportRichDisabled.setText("Don't send from Rich/Artifacts planets");
        transportRichDisabled.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                transportRichDisabledActionPerformed(evt);
            }
        });

        transportPoorDouble.setText("Send double from Poor planets");
        transportPoorDouble.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                transportPoorDoubleActionPerformed(evt);
            }
        });

        autotransportXilmi.setText("Let AI handle population transportation");
        autotransportXilmi.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                autotransportXilmiActionPerformed(evt);
            }
        });

        allowUngoverned.setText("allow sending population from ungoverned colonies");
        allowUngoverned.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                allowUngovernedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout autotransportPanelLayout = new javax.swing.GroupLayout(autotransportPanel);
        autotransportPanel.setLayout(autotransportPanelLayout);
        autotransportPanelLayout.setHorizontalGroup(
            autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(autotransportPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(autotransportPanelLayout.createSequentialGroup()
                        .addComponent(transportMaxTurns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(transportMaxTurnsLabel))
                    .addComponent(transportMaxTurnsNebula)
                    .addComponent(transportRichDisabled)
                    .addComponent(transportPoorDouble)
                    .addGroup(autotransportPanelLayout.createSequentialGroup()
                        .addGroup(autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(autotransport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(autotransportPanelLayout.createSequentialGroup()
                                .addComponent(autotransportXilmi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(187, 187, 187))
                            .addGroup(autotransportPanelLayout.createSequentialGroup()
                                .addComponent(allowUngoverned, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(187, 187, 187)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        autotransportPanelLayout.setVerticalGroup(
            autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(autotransportPanelLayout.createSequentialGroup()
                .addComponent(autotransportXilmi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(allowUngoverned)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autotransport)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transportMaxTurns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transportMaxTurnsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transportMaxTurnsNebula)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transportRichDisabled)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transportPoorDouble))
        );

        allGovernorsOn.setText("All Governors ON");
        allGovernorsOn.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                allGovernorsOnActionPerformed(evt);
            }
        });

        allGovernorsOff.setText("All Governors OFF");
        allGovernorsOff.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                allGovernorsOffActionPerformed(evt);
            }
        });

        stargatePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Stargate Options"));

        stargateOptions.add(stargateOff);
        stargateOff.setText("Never build stargates");
        stargateOff.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                stargateOffActionPerformed(evt);
            }
        });

        stargateOptions.add(stargateRich);
        stargateRich.setText("Build stargates on Rich and Ultra Rich planets");
        stargateRich.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                stargateRichActionPerformed(evt);
            }
        });

        stargateOptions.add(stargateOn);
        stargateOn.setText("Always build stargates");
        stargateOn.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                stargateOnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout stargatePanelLayout = new javax.swing.GroupLayout(stargatePanel);
        stargatePanel.setLayout(stargatePanelLayout);
        stargatePanelLayout.setHorizontalGroup(
            stargatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stargatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(stargatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stargateOff)
                    .addComponent(stargateRich)
                    .addComponent(stargateOn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        stargatePanelLayout.setVerticalGroup(
            stargatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stargatePanelLayout.createSequentialGroup()
                .addComponent(stargateOff)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stargateRich)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stargateOn))
        );

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        missileBases.setModel(new javax.swing.SpinnerNumberModel(0, 0, 20, 1));
        missileBases.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                missileBasesStateChanged(evt);
            }
        });
        missileBases.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                missileBasesMouseWheelMoved(evt);
            }
        });

        missileBasesLabel.setText("Minimum missile bases");

        autospend.setText("Autospend");
        autospend.setToolTipText("Automatically spend reserve on planets with lowest production");
        autospend.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                autospendActionPerformed(evt);
            }
        });

        reserve.setModel(new javax.swing.SpinnerNumberModel(1000, 0, 100000, 10));
        reserve.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                reserveStateChanged(evt);
            }
        });
        reserve.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                reserveMouseWheelMoved(evt);
            }
        });

        resrveLabel.setText("Keep in reserve");

        shipbuilding.setText("Shipbuilding with Governor enabled");
        shipbuilding.setToolTipText("Divert resources into shipbuilding and not research if planet is already building ships");
        shipbuilding.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                shipbuildingActionPerformed(evt);
            }
        });

        autoScout.setText("Auto Scout");
        autoScout.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoScoutActionPerformed(evt);
            }
        });

        autoColonize.setText("Auto Colonize");
        autoColonize.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoColonizeActionPerformed(evt);
            }
        });

        completionist.setText("Completionist Technologies");
        completionist.setToolTipText("<html>\nI like completing games fully. <br/>\nAllow all Empires to Research the following Technologies:<br/>\n<br/>\nControlled Irradiated Environment<br/>\nAtmospheric Terraforming<br/>\nComplete Terraforming<br/>\nAdvanced Soil Enrichment<br/>\nIntergalactic Star Gates<br/>\n<br/>\nMore than 30% of the Galaxy needs to be colonized.<br/>\nPlayer must control more than 50% of colonized systems.<br/>\nPlayer must have completed all Research in their Tech Tree (Future Techs too).<br/>\n</html>");
        completionist.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                completionistActionPerformed(evt);
            }
        });

        autoAttack.setText("Auto Attack");
        autoAttack.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoAttackActionPerformed(evt);
            }
        });

        autoColonyShipCount.setModel(new javax.swing.SpinnerNumberModel(1, 1, 9999, 1));
        autoColonyShipCount.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                autoColonyShipCountStateChanged(evt);
            }
        });
        autoColonyShipCount.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                autoColonyShipCountMouseWheelMoved(evt);
            }
        });

        autoColonyShipCountLabel.setText("Number of colony ships to send");

        autoScoutShipCount.setModel(new javax.swing.SpinnerNumberModel(1, 1, 9999, 1));
        autoScoutShipCount.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                autoScoutShipCountStateChanged(evt);
            }
        });
        autoScoutShipCount.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                autoScoutShipCountMouseWheelMoved(evt);
            }
        });

        autoAttackShipCount.setModel(new javax.swing.SpinnerNumberModel(1, 1, 9999, 1));
        autoAttackShipCount.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                autoAttackShipCountStateChanged(evt);
            }
        });
        autoAttackShipCount.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                autoAttackShipCountMouseWheelMoved(evt);
            }
        });

        autoScoutShipCountLabel.setText("Number of scout ships to send");

        autoAttackShipCountLabel.setText("Number of attack ships to send");

        shieldWithoutBases.setText("Allow shields without bases");
        shieldWithoutBases.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                shieldWithoutBasesActionPerformed(evt);
            }
        });

        legacyGrowthMode.setText("Develop colonies as quickly as possible");
        legacyGrowthMode.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                legacyGrowthModeActionPerformed(evt);
            }
        });

        autoSpy.setText("Let AI handle spies");
        autoSpy.setToolTipText("Hand control over spies to AI");
        autoSpy.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoSpyActionPerformed(evt);
            }
        });

        autoInfiltrate.setText("Autoinfiltrate");
        autoInfiltrate.setToolTipText("Automatically sends spies to infiltrate other empires");
        autoInfiltrate.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoInfiltrateActionPerformed(evt);
            }
        });

        applyButton.setText("Apply");
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });

        autoApplyToggleButton.setSelected(true);
        autoApplyToggleButton.setText("Auto Apply");
        autoApplyToggleButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoApplyToggleButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(autotransportPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stargatePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(allGovernorsOn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(allGovernorsOff))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(governorDefault)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(autoColonize)
                                    .addComponent(autoScout)
                                    .addComponent(autoAttack))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(autoAttackShipCount)
                                    .addComponent(autoScoutShipCount)
                                    .addComponent(autoColonyShipCount))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(autoColonyShipCountLabel)
                                    .addComponent(autoScoutShipCountLabel)
                                    .addComponent(autoAttackShipCountLabel))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(okButton)
                        .addGap(18, 18, 18)
                        .addComponent(applyButton)
                        .addGap(18, 18, 18)
                        .addComponent(autoApplyToggleButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(shipbuilding)
                                    .addComponent(autoInfiltrate, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(9, 9, 9)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(autoSpy, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(completionist)
                                        .addGap(8, 8, 8))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(legacyGrowthMode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(193, 193, 193))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(autospend)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(reserve, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(resrveLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(missileBases, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(missileBasesLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(shieldWithoutBases)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(governorDefault)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autotransportPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(allGovernorsOn)
                    .addComponent(allGovernorsOff))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stargatePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(autoScout)
                    .addComponent(autoScoutShipCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(autoScoutShipCountLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(autoColonize)
                    .addComponent(autoColonyShipCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(autoColonyShipCountLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(autoAttack)
                    .addComponent(autoAttackShipCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(autoAttackShipCountLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(missileBases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(missileBasesLabel)
                    .addComponent(shieldWithoutBases))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(autospend)
                    .addComponent(reserve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resrveLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(shipbuilding)
                    .addComponent(legacyGrowthMode))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(completionist)
                    .addComponent(autoSpy)
                    .addComponent(autoInfiltrate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton)
                    .addComponent(applyButton)
                    .addComponent(autoApplyToggleButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void allGovernorsOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allGovernorsOnActionPerformed
        for (StarSystem ss : GameSession.instance().galaxy().player().orderedColonies()) {
            if (!ss.isColonized()) {
                // shouldn't happen
                continue;
            }
            ss.colony().setGovernor(true);
            ss.colony().governIfNeeded();
            if (autoApplyToggleButton.isEnabled()) { // BR:
                GovernorOptions options = GameSession.instance().getGovernorOptions();
                options.setGovernorOnByDefault(governorDefault.isSelected());
            }
        }
    }//GEN-LAST:event_allGovernorsOnActionPerformed

    private void allGovernorsOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allGovernorsOffActionPerformed
        for (StarSystem ss : GameSession.instance().galaxy().player().orderedColonies()) {
            if (!ss.isColonized()) {
                // shouldn't happen
                continue;
            }
            ss.colony().setGovernor(false);
            if (autoApplyToggleButton.isEnabled()) { // BR:
                GovernorOptions options = GameSession.instance().getGovernorOptions();
                options.setGovernorOnByDefault(governorDefault.isSelected());
            }
        }
    }//GEN-LAST:event_allGovernorsOffActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
    	applyAction();
    	frame.setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
    	applyAction();
    }//GEN-LAST:event_applyButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        frame.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void missileBasesMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_missileBasesMouseWheelMoved
        mouseWheel(missileBases, evt);
    }//GEN-LAST:event_missileBasesMouseWheelMoved

    private void reserveMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_reserveMouseWheelMoved
        mouseWheel(reserve, evt);
    }//GEN-LAST:event_reserveMouseWheelMoved

    private void completionistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_completionistActionPerformed
        performCompletionist();
    }//GEN-LAST:event_completionistActionPerformed

    private void autoColonyShipCountMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_autoColonyShipCountMouseWheelMoved
        mouseWheel(autoColonyShipCount, evt);
    }//GEN-LAST:event_autoColonyShipCountMouseWheelMoved

    private void autoScoutShipCountMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_autoScoutShipCountMouseWheelMoved
        mouseWheel(autoScoutShipCount, evt);
    }//GEN-LAST:event_autoScoutShipCountMouseWheelMoved

    private void autoAttackShipCountMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_autoAttackShipCountMouseWheelMoved
        mouseWheel(autoAttackShipCount, evt);
    }//GEN-LAST:event_autoAttackShipCountMouseWheelMoved

    private void autotransportXilmiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autotransportXilmiActionPerformed
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutotransportXilmi(autotransportXilmi.isSelected());
        }
    }//GEN-LAST:event_autotransportXilmiActionPerformed

    private void transportMaxTurnsLabelMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_transportMaxTurnsLabelMouseWheelMoved
        mouseWheel(transportMaxTurns, evt);
    }//GEN-LAST:event_transportMaxTurnsLabelMouseWheelMoved

    private void transportMaxTurnsMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_transportMaxTurnsMouseWheelMoved
        mouseWheel(transportMaxTurns, evt);
    }//GEN-LAST:event_transportMaxTurnsMouseWheelMoved

    private void autoApplyToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoApplyToggleButtonActionPerformed
        GovernorOptions options = GameSession.instance().getGovernorOptions();
        options.setAutoApply(autoApplyToggleButton.isEnabled());
        if (autoApplyToggleButton.isEnabled()) { // BR:
        	applyAction();
        }
    }//GEN-LAST:event_autoApplyToggleButtonActionPerformed

    private void allowUngovernedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allowUngovernedActionPerformed
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutotransportUngoverned(allowUngoverned.isSelected());
        }
    }//GEN-LAST:event_allowUngovernedActionPerformed

    private void autotransportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autotransportActionPerformed
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutotransport(autotransport.isSelected());
        }
    }//GEN-LAST:event_autotransportActionPerformed

    private void transportRichDisabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transportRichDisabledActionPerformed
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setTransportRichDisabled(transportRichDisabled.isSelected());
        }
    }//GEN-LAST:event_transportRichDisabledActionPerformed

    private void transportPoorDoubleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transportPoorDoubleActionPerformed
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setTransportPoorDouble(transportPoorDouble.isSelected());
        }
    }//GEN-LAST:event_transportPoorDoubleActionPerformed

    private void autoScoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoScoutActionPerformed
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutoScout(autoScout.isSelected());
        }
    }//GEN-LAST:event_autoScoutActionPerformed

    private void autoColonizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoColonizeActionPerformed
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutoColonize(autoColonize.isSelected());
        }
    }//GEN-LAST:event_autoColonizeActionPerformed

    private void autoAttackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoAttackActionPerformed
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutoAttack(autoAttack.isSelected());
        }
    }//GEN-LAST:event_autoAttackActionPerformed

    private void shieldWithoutBasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shieldWithoutBasesActionPerformed
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setShieldWithoutBases(shieldWithoutBases.isSelected());
        }
    }//GEN-LAST:event_shieldWithoutBasesActionPerformed

    private void autospendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autospendActionPerformed
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutospend(autospend.isSelected());
        }
    }//GEN-LAST:event_autospendActionPerformed

    private void shipbuildingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shipbuildingActionPerformed
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setShipbuilding(shipbuilding.isSelected());
        }
    }//GEN-LAST:event_shipbuildingActionPerformed

    private void autoInfiltrateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoInfiltrateActionPerformed
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutoInfiltrate(autoInfiltrate.isSelected());
        }
    }//GEN-LAST:event_autoInfiltrateActionPerformed

    private void legacyGrowthModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_legacyGrowthModeActionPerformed
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setLegacyGrowthMode(legacyGrowthMode.isSelected());
        }
    }//GEN-LAST:event_legacyGrowthModeActionPerformed

    private void autoSpyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoSpyActionPerformed
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutoSpy(autoSpy.isSelected());
        }
    }//GEN-LAST:event_autoSpyActionPerformed

    private void stargateOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stargateOffActionPerformed
        if (autoApplyToggleButton.isEnabled()) applyStargates(); // BR:
    }//GEN-LAST:event_stargateOffActionPerformed

    private void stargateRichActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stargateRichActionPerformed
        if (autoApplyToggleButton.isEnabled()) applyStargates(); // BR:
    }//GEN-LAST:event_stargateRichActionPerformed

    private void stargateOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stargateOnActionPerformed
        if (autoApplyToggleButton.isEnabled()) applyStargates(); // BR:
    }//GEN-LAST:event_stargateOnActionPerformed

    private void reserveStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_reserveStateChanged
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setReserve((Integer)reserve.getValue());
        }
    }//GEN-LAST:event_reserveStateChanged

    private void missileBasesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_missileBasesStateChanged
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setMinimumMissileBases((Integer)missileBases.getValue());
        }
    }//GEN-LAST:event_missileBasesStateChanged

    private void autoAttackShipCountStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_autoAttackShipCountStateChanged
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutoAttackShipCount((Integer)autoAttackShipCount.getValue());
        }
    }//GEN-LAST:event_autoAttackShipCountStateChanged

    private void autoColonyShipCountStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_autoColonyShipCountStateChanged
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutoColonyShipCount((Integer)autoColonyShipCount.getValue());
        }
    }//GEN-LAST:event_autoColonyShipCountStateChanged

    private void autoScoutShipCountStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_autoScoutShipCountStateChanged
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutoScoutShipCount((Integer)autoScoutShipCount.getValue());
        }
   }//GEN-LAST:event_autoScoutShipCountStateChanged

    private void transportMaxTurnsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_transportMaxTurnsStateChanged
        if (autoApplyToggleButton.isEnabled()) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setTransportMaxTurns((Integer)transportMaxTurns.getValue());
        }
    }//GEN-LAST:event_transportMaxTurnsStateChanged

    private static void mouseWheel(JSpinner spinner, java.awt.event.MouseWheelEvent evt) {
        if (evt.getScrollType() != MouseWheelEvent.WHEEL_UNIT_SCROLL) {
            return;
        }
        SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
        int value = (int) model.getValue();
        // always scroll integers by 1
        value -= Math.signum(evt.getUnitsToScroll()) * model.getStepSize().intValue();
        int minimum = ((Number)model.getMinimum()).intValue();
        int maximum = ((Number)model.getMaximum()).intValue();
        if (value < minimum) {
            value = minimum;
        }
        if (value > maximum) {
            value = maximum;
        }
        spinner.setValue(value);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton allGovernorsOff;
    private javax.swing.JButton allGovernorsOn;
    private javax.swing.JCheckBox allowUngoverned;
    private javax.swing.JButton applyButton;
    private javax.swing.JToggleButton autoApplyToggleButton;
    private javax.swing.JCheckBox autoAttack;
    private javax.swing.JSpinner autoAttackShipCount;
    private javax.swing.JLabel autoAttackShipCountLabel;
    private javax.swing.JCheckBox autoColonize;
    private javax.swing.JSpinner autoColonyShipCount;
    private javax.swing.JLabel autoColonyShipCountLabel;
    private javax.swing.JCheckBox autoInfiltrate;
    private javax.swing.JCheckBox autoScout;
    private javax.swing.JSpinner autoScoutShipCount;
    private javax.swing.JLabel autoScoutShipCountLabel;
    private javax.swing.JCheckBox autoSpy;
    private javax.swing.JCheckBox autospend;
    private javax.swing.JCheckBox autotransport;
    private javax.swing.JCheckBox autotransportXilmi;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton completionist;
    private javax.swing.JCheckBox governorDefault;
    private javax.swing.JCheckBox legacyGrowthMode;
    private javax.swing.JSpinner missileBases;
    private javax.swing.JLabel missileBasesLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JSpinner reserve;
    private javax.swing.JLabel resrveLabel;
    private javax.swing.JCheckBox shieldWithoutBases;
    private javax.swing.JCheckBox shipbuilding;
    private javax.swing.JRadioButton stargateOff;
    private javax.swing.JRadioButton stargateOn;
    private javax.swing.ButtonGroup stargateOptions;
    private javax.swing.JRadioButton stargateRich;
    private javax.swing.JSpinner transportMaxTurns;
    private javax.swing.JCheckBox transportPoorDouble;
    private javax.swing.JCheckBox transportRichDisabled;
    // End of variables declaration//GEN-END:variables

    // Just test the layout
    public static void main(String arg[]) {
        // initialize everything
        RotPUI.instance();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
			public void run() {
                JFrame frame = new JFrame("GovernorOptions");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                //Create and set up the content pane.
                GovernorOptionsPanel newContentPane = new GovernorOptionsPanel(frame);
                newContentPane.setOpaque(true); //content panes must be opaque
                frame.setContentPane(newContentPane);

                //Display the window.
                frame.pack();
                frame.setVisible(true);
            }
        });

    }
}
