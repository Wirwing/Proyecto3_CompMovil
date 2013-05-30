package com.fmat.proyecto3.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Define un DialogFragment que muestra el diálogo de error 
 * @author Irving Caro
 *
 */
public class ErrorDialogFragment extends DialogFragment {
	// Global field to contain the error dialog
	private Dialog mDialog;

	/**
	 * Constructor por defecto. Establce el campo del diálogo como null
	 */
	public ErrorDialogFragment() {
		super();
		mDialog = null;
	}

	/**
	 * Establece el diálogo a mostrar
	 * @param dialog
	 */
	public void setDialog(Dialog dialog) {
		mDialog = dialog;
	}

	/**
	 * Retorna el framgmento del diálogo
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return mDialog;
	}
}