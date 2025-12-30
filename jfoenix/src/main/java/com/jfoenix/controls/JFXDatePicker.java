/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.jfoenix.controls;

import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.base.IFXValidatableControl;
import com.jfoenix.adapters.ReflectionHelper;
import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.skins.JFXDatePickerSkin;
import com.jfoenix.validation.base.ValidatorBase;
import com.sun.javafx.binding.ExpressionHelper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.css.*;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.PaintConverter;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JFXDatePicker is the material design implementation of a date picker.
 *
 * @author Shadi Shaheen
 * @version 1.0
 * @since 2016-03-09
 */
public class JFXDatePicker extends DatePicker implements IFXValidatableControl {

    /**
     * {@inheritDoc}
     */
    public JFXDatePicker() {
        super();
        System.out.println("JFXDatePicker() called");
        initialize();
    }

    /**
     * {@inheritDoc}
     */
    public JFXDatePicker(LocalDate localDate) {
        super(localDate);
        initialize();
    }

    private void initialize() {
        System.out.println("JFXDatePicker -> init() called");

        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        editorProperty();
        System.out.println("editorProperty() was called");

        ReadOnlyObjectWrapper<TextField> editor = ReflectionHelper.getFieldContent(DatePicker.class, this, "editor");

        final FakeFocusJFXTextField editorNode = new FakeFocusJFXTextField();
        this.focusedProperty().addListener((obj, oldVal, newVal) -> {
            if (getEditor() != null) {
                editorNode.setFakeFocus(newVal);
            }
        });
        editorNode.activeValidatorWritableProperty().bind(activeValidatorProperty());
        editor.set(editorNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserAgentStylesheet() {
        return JFoenixResources.load("css/controls/jfx-date-picker.css").toExternalForm();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        System.out.println("JFXDatePicker -> createDefaultSkin() is called!");

        dumpChangeListeners(this.focusedProperty());

        return new JFXDatePickerSkin(this);
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * the parent node used when showing the data picker content as an overlay,
     * intead of a popup
     */
    private ObjectProperty<StackPane> dialogParent = new SimpleObjectProperty<>(null);

    public final ObjectProperty<StackPane> dialogParentProperty() {
        return this.dialogParent;
    }

    public final StackPane getDialogParent() {
        return this.dialogParentProperty().get();
    }

    public final void setDialogParent(final StackPane dialogParent) {
        this.dialogParentProperty().set(dialogParent);
    }

    private ValidationControl validationControl = new ValidationControl(this);

    @Override
    public ValidatorBase getActiveValidator() {
        return validationControl.getActiveValidator();
    }

    @Override
    public ReadOnlyObjectProperty<ValidatorBase> activeValidatorProperty() {
        return validationControl.activeValidatorProperty();
    }

    @Override
    public ObservableList<ValidatorBase> getValidators() {
        return validationControl.getValidators();
    }

    @Override
    public void setValidators(ValidatorBase... validators) {
        validationControl.setValidators(validators);
    }

    @Override
    public boolean validate() {
        return validationControl.validate();
    }

    @Override
    public void resetValidation() {
        validationControl.resetValidation();
    }

    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    /**
     * Initialize the style class to 'jfx-date-picker'.
     * <p>
     * This is the selector class from which CSS can be used to style
     * this control.
     */
    private static final String DEFAULT_STYLE_CLASS = "jfx-date-picker";

    /**
     * show the popup as an overlay using JFXDialog
     * NOTE: to show it properly the scene root must be StackPane, or the user must set
     * the dialog parent manually using the property {{@link #dialogParentProperty()}
     */
    private StyleableBooleanProperty overLay = new SimpleStyleableBooleanProperty(StyleableProperties.OVERLAY,
            JFXDatePicker.this,
            "overLay",
            false);

    public final StyleableBooleanProperty overLayProperty() {
        return this.overLay;
    }

    public final boolean isOverLay() {
        return overLay != null && this.overLayProperty().get();
    }

    public final void setOverLay(final boolean overLay) {
        this.overLayProperty().set(overLay);
    }

    /**
     * the default color used in the data picker content
     */
    private StyleableObjectProperty<Paint> defaultColor = new SimpleStyleableObjectProperty<>(StyleableProperties.DEFAULT_COLOR,
            JFXDatePicker.this,
            "defaultColor",
            Color.valueOf(
                    "#009688"));

    public Paint getDefaultColor() {
        return defaultColor == null ? Color.valueOf("#009688") : defaultColor.get();
    }

    public StyleableObjectProperty<Paint> defaultColorProperty() {
        return this.defaultColor;
    }

    public void setDefaultColor(Paint color) {
        this.defaultColor.set(color);
    }


    private static class StyleableProperties {
        private static final CssMetaData<JFXDatePicker, Paint> DEFAULT_COLOR =
                new CssMetaData<JFXDatePicker, Paint>("-jfx-default-color",
                        PaintConverter.getInstance(), Color.valueOf("#009688")) {
                    @Override
                    public boolean isSettable(JFXDatePicker control) {
                        return control.defaultColor == null || !control.defaultColor.isBound();
                    }

                    @Override
                    public StyleableProperty<Paint> getStyleableProperty(JFXDatePicker control) {
                        return control.defaultColorProperty();
                    }
                };

        private static final CssMetaData<JFXDatePicker, Boolean> OVERLAY =
                new CssMetaData<JFXDatePicker, Boolean>("-jfx-overlay",
                        BooleanConverter.getInstance(), false) {
                    @Override
                    public boolean isSettable(JFXDatePicker control) {
                        return control.overLay == null || !control.overLay.isBound();
                    }

                    @Override
                    public StyleableBooleanProperty getStyleableProperty(JFXDatePicker control) {
                        return control.overLayProperty();
                    }
                };

        private static final List<CssMetaData<? extends Styleable, ?>> CHILD_STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                    new ArrayList<>(DatePicker.getClassCssMetaData());
            Collections.addAll(styleables,
                    DEFAULT_COLOR,
                    OVERLAY);
            CHILD_STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.CHILD_STYLEABLES;
    }


    static void dumpChangeListeners(ReadOnlyBooleanProperty prop) {
        try {
            Object helper = findHelper(prop);

            System.out.println("c1 = " + prop.getClass());
            System.out.println("c2 = " + prop.getClass().getSuperclass());
            System.out.println("c3 = " + prop.getClass().getSuperclass().getSuperclass());


            if (helper == null) {
                System.out.println("No helper found");
                return;
            } else {
                System.out.println("Helper class: " + helper.getClass());
            }

            if (helper.getClass().getSimpleName().equals("SingleChange")) {
                ChangeListener<?> l =
                        ReflectionHelper.getFieldContent(helper.getClass(), helper, "listener");

                System.out.println("=== Single ChangeListener ===");
                if (l != null) dumpListener(l);
                return;
            }

            if (helper.getClass().getSimpleName().equals("Generic")) {
                ChangeListener<?>[] listeners =
                        ReflectionHelper.getFieldContent(helper.getClass(), helper, "changeListeners");

                System.out.println("=== Multiple ChangeListeners ===");

                if (listeners != null) {
                    for (int i = 0; i < listeners.length; i++) {
                        if (listeners[i] != null) {
                            System.out.println("[" + i + "]");
                            dumpListener(listeners[i]);
                        }
                    }
                }
                return;
            }

            System.out.println("Unknown helper type");

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    static void dumpListener(ChangeListener<?> l) {
        System.out.println("  Listener class: " + l.getClass().getName());

        var cs = l.getClass().getProtectionDomain().getCodeSource();
        System.out.println("  CodeSource: " + (cs == null ? "null" : cs.getLocation()));

        // Stack traces are limited in native image – so this is optional
    }

    static Object findHelper(ReadOnlyBooleanProperty prop) {
        if ("Substrate VM".equals(System.getProperty("java.vm.name"))) {
            Class<?> c = prop.getClass().getSuperclass().getSuperclass();
            if (c != null) {
                return ReflectionHelper.getFieldContent(c, prop, "helper");
            }
        } else {
            Class<?> c = prop.getClass().getSuperclass();
            if (c != null) {
                return ReflectionHelper.getFieldContent(c, prop, "helper");
            }
        }
        return null;
    }

    static void dumpListenerOrigin(ChangeListener<?> l) {
        try {
            System.out.println("  -> declared in: "
                    + l.getClass().getProtectionDomain().getCodeSource());

            for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
                if (ste.getClassName().contains("ComboBox")
                        || ste.getClassName().contains("DatePicker")
                        || ste.getClassName().contains("Skin")) {
                    System.out.println("     " + ste);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
