package com.example.tictactoe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PopUp extends AppCompatDialogFragment {
    String clickButton;
    public PopUp(String textInput){
        clickButton = textInput;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        builder.setTitle(clickButton);
        // State what needs to be displayed
        builder.setPositiveButton("OKAY", (dialogInterface, i) ->
        {
            PlayGameActivity game = (PlayGameActivity) getActivity();
            // if game isn't empty, should reset the board
            if (game != null)
            {
                // prompts a reset blank new game board
                game.resetBoard();
            }
        }
        );
        // return the builder
        return builder.create();
    }

}
