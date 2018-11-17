package of.media.bq.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import of.media.bq.R;

public class DialpadFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener{

    private static final String TAG = "BT.DialpadFragment";

    private TextView phoneNumber;
    private ImageButton deleteButton;
    private ImageButton dialButton;

    private Button buttonOne;
    private Button buttonTwo;
    private Button buttonThree;
    private Button buttonFour;
    private Button buttonFive;
    private Button buttonSix;
    private Button buttonSeven;
    private Button buttonEight;
    private Button buttonNine;
    private Button buttonZero;
    private Button buttonStar;
    private Button buttonPound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.bluetooth_dialpad, container, false);

        initViews(view);

        return view;
    }

    /**
     * Initializes the View components
     */
    private void initViews(View view) {
        initializeViews(view);
        addNumberFormatting();
        setClickListeners();
    }

    /**
     * Initializes the views from XML
     */
    private void initializeViews(View view) {

        phoneNumber  = view.findViewById(R.id.dialpad_phone_number);
        phoneNumber.setInputType(android.text.InputType.TYPE_NULL);

        deleteButton = view.findViewById(R.id.dialpad_delete_button);
        dialButton   = view.findViewById(R.id.dialpad_dial_button);

        buttonOne    = view.findViewById(R.id.dialpad_number_one);
        buttonTwo    = view.findViewById(R.id.dialpad_number_two);
        buttonThree  = view.findViewById(R.id.dialpad_number_three);
        buttonFour   = view.findViewById(R.id.dialpad_number_four);
        buttonFive   = view.findViewById(R.id.dialpad_number_five);
        buttonSix    = view.findViewById(R.id.dialpad_number_six);
        buttonSeven  = view.findViewById(R.id.dialpad_number_seven);
        buttonEight  = view.findViewById(R.id.dialpad_number_eight);
        buttonNine   = view.findViewById(R.id.dialpad_number_nine);
        buttonZero   = view.findViewById(R.id.dialpad_number_zero);
        buttonStar   = view.findViewById(R.id.dialpad_number_star);
        buttonPound  = view.findViewById(R.id.dialpad_number_pound);
    }

    /**
     * Adds number formatting to the field
     */
    private void addNumberFormatting() {
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    /**
     * Sets click listeners for the views
     */
    private void setClickListeners() {

        deleteButton.setOnClickListener(this);
        deleteButton.setOnLongClickListener(this);

        dialButton.setOnClickListener(this);

        buttonZero.setOnClickListener(this);
        buttonZero.setOnLongClickListener(this);

        buttonOne.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);
        buttonThree.setOnClickListener(this);
        buttonFour.setOnClickListener(this);
        buttonFive.setOnClickListener(this);
        buttonSix.setOnClickListener(this);
        buttonSeven.setOnClickListener(this);
        buttonEight.setOnClickListener(this);
        buttonNine.setOnClickListener(this);
        buttonStar.setOnClickListener(this);
        buttonPound.setOnClickListener(this);
    }

    private void keyPressed(int keyCode) {
        Log.d(TAG, "keyPressed");
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        phoneNumber.onKeyDown(keyCode, event);
    }

    /**
     * Click handler for the views
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialpad_number_one: {
                keyPressed(KeyEvent.KEYCODE_1);
                break;
            }
            case R.id.dialpad_number_two: {
                keyPressed(KeyEvent.KEYCODE_2);
                break;
            }
            case R.id.dialpad_number_three: {
                keyPressed(KeyEvent.KEYCODE_3);
                break;
            }
            case R.id.dialpad_number_four: {
                keyPressed(KeyEvent.KEYCODE_4);
                break;
            }
            case R.id.dialpad_number_five: {
                keyPressed(KeyEvent.KEYCODE_5);
                break;
            }
            case R.id.dialpad_number_six: {
                keyPressed(KeyEvent.KEYCODE_6);
                break;
            }
            case R.id.dialpad_number_seven: {
                keyPressed(KeyEvent.KEYCODE_7);
                break;
            }
            case R.id.dialpad_number_eight: {
                keyPressed(KeyEvent.KEYCODE_8);
                break;
            }
            case R.id.dialpad_number_nine: {
                keyPressed(KeyEvent.KEYCODE_9);
                break;
            }
            case R.id.dialpad_number_zero: {
                keyPressed(KeyEvent.KEYCODE_0);
                break;
            }
            case R.id.dialpad_number_pound: {
                keyPressed(KeyEvent.KEYCODE_POUND);
                break;
            }
            case R.id.dialpad_number_star: {
                keyPressed(KeyEvent.KEYCODE_STAR);
                break;
            }
            case R.id.dialpad_delete_button: {
                keyPressed(KeyEvent.KEYCODE_DEL);
                break;
            }
            case R.id.dialpad_dial_button: {
                dialNumber();
                break;
            }
        }

    }

    /**
     * Long Click Listener
     */
    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.dialpad_delete_button: {
                phoneNumber.setText("");
                return true;
            }
            case R.id.dialpad_number_zero: {
                keyPressed(KeyEvent.KEYCODE_PLUS);
                return true;
            }
        }
        return false;
    }

    /**
     * Starts the bt dial action
     */
    private void dialNumber() {
        String number = phoneNumber.getText().toString();
        if (number.length() > 0) {
            Log.d(TAG, "dialNumber " + number);
        }
    }

}
