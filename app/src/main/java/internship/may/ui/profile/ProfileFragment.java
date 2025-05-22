package internship.may.ui.profile;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import internship.may.ConstantSp;
import internship.may.MainActivity;
import internship.may.R;
import internship.may.SignupActivity;
import internship.may.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    SharedPreferences sp;
    //String[] countryArray = { "India", "USA", "UK", "Canada" };
    ArrayList<String> countryArray;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    SQLiteDatabase db;
    String sGender, sCountry;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sp = getActivity().getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        db = getActivity().openOrCreateDatabase("InternshipMay.db", MODE_PRIVATE, null);

        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS (USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),COUNTRY VARCHAR(20))";
        db.execSQL(tableQuery);

        binding.profileGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = root.findViewById(i);
                sGender = radioButton.getText().toString();
                Toast.makeText(getActivity(), sGender, Toast.LENGTH_SHORT).show();
            }
        });

        countryArray = new ArrayList<>();
        countryArray.add("India");
        countryArray.add("UK");
        countryArray.add("USA");
        countryArray.add("Canada");
        countryArray.add("Demo");
        countryArray.add("Austrlia");

        countryArray.add(0, "Select Country");

        countryArray.remove(5);
        countryArray.set(5, "Australia");

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, countryArray);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        binding.profileCountry.setAdapter(adapter);

        binding.profileCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    sCountry = "";
                } else {
                    sCountry = countryArray.get(i);
                    Toast.makeText(getActivity(), sCountry, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.profileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.profileName.getText().toString().trim().equals("")) {
                    binding.profileName.setError("Name Required");
                } else if (binding.profileEmail.getText().toString().trim().equals("")) {
                    binding.profileEmail.setError("Email Required");
                } else if (!binding.profileEmail.getText().toString().trim().matches(emailPattern)) {
                    binding.profileEmail.setError("Valid Email Id Required");
                } else if (binding.profileContact.getText().toString().trim().equals("")) {
                    binding.profileContact.setError("Contact No. Required");
                } else if (binding.profileContact.getText().toString().trim().length() < 10) {
                    binding.profileContact.setError("Valid Contact No. Required");
                } else if (binding.profilePassword.getText().toString().trim().equals("")) {
                    binding.profilePassword.setError("Password Required");
                } else if (binding.profilePassword.getText().toString().trim().length() < 6) {
                    binding.profilePassword.setError("Min. 6 Char Password Required");
                } else if (binding.profileConfirmPassword.getText().toString().trim().equals("")) {
                    binding.profileConfirmPassword.setError("Confirm Password Required");
                } else if (binding.profileConfirmPassword.getText().toString().trim().length() < 6) {
                    binding.profileConfirmPassword.setError("Min. 6 Char Confirm Password Required");
                } else if (!binding.profileConfirmPassword.getText().toString().trim().matches(binding.profilePassword.getText().toString().trim())) {
                    binding.profileConfirmPassword.setError("Password Does Not Match");
                } else if (binding.profileGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity(), "Please Select Gender", Toast.LENGTH_SHORT).show();
                } else if (binding.profileCountry.getSelectedItemPosition() <= 0) {
                    Toast.makeText(getActivity(), "Please Select Country", Toast.LENGTH_SHORT).show();
                } else {
                    String updateQuery = "UPDATE USERS SET NAME='"+binding.profileName.getText().toString()+"',EMAIL='"+binding.profileEmail.getText().toString()+"',CONTACT='"+binding.profileContact.getText().toString()+"',PASSWORD='"+binding.profilePassword.getText().toString()+"',GENDER='"+sGender+"',COUNTRY='"+sCountry+"' WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"'";
                    db.execSQL(updateQuery);
                    Toast.makeText(getActivity(), "Profile Update Successfully", Toast.LENGTH_SHORT).show();

                    sp.edit().putString(ConstantSp.NAME,binding.profileName.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.EMAIL,binding.profileEmail.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.CONTACT,binding.profileContact.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.PASSWORD,binding.profilePassword.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.GENDER,sGender).commit();
                    sp.edit().putString(ConstantSp.COUNTRY,sCountry).commit();

                    setData(false);
                }
            }
        });

        binding.profileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Logout!!!");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage("Are you sure want to logout!");

                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sp.edit().clear().commit();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                });

                builder.setNeutralButton("Rate Us", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Rate Us", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                });

                builder.show();

            }
        });

        setData(false);

        binding.profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(true);
            }
        });

        binding.profileDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete Account");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage("Are you sure want to delete account?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String deleteQuery = "DELETE FROM USERS WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"'";
                        db.execSQL(deleteQuery);
                        Toast.makeText(getActivity(), "Account Deleted Successfully", Toast.LENGTH_SHORT).show();
                        sp.edit().clear().commit();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.show();

            }
        });

        return root;
    }

    private void setData(boolean b) {
        if (b) {
            binding.profileConfirmPassword.setVisibility(View.VISIBLE);
            binding.profileUpdate.setVisibility(View.VISIBLE);
            binding.profileEdit.setVisibility(View.GONE);
        } else {
            binding.profileConfirmPassword.setVisibility(View.GONE);
            binding.profileUpdate.setVisibility(View.GONE);
            binding.profileEdit.setVisibility(View.VISIBLE);
        }

        binding.profileName.setEnabled(b);
        binding.profileEmail.setEnabled(b);
        binding.profileContact.setEnabled(b);
        binding.profilePassword.setEnabled(b);
        binding.profileMale.setEnabled(b);
        binding.profileFemale.setEnabled(b);
        binding.profileCountry.setEnabled(b);

        binding.profileName.setText(sp.getString(ConstantSp.NAME, ""));
        binding.profileEmail.setText(sp.getString(ConstantSp.EMAIL, ""));
        binding.profileContact.setText(sp.getString(ConstantSp.CONTACT, ""));
        binding.profilePassword.setText(sp.getString(ConstantSp.PASSWORD, ""));
        binding.profileConfirmPassword.setText(sp.getString(ConstantSp.PASSWORD, ""));

        sGender = sp.getString(ConstantSp.GENDER, "");
        if (sGender.equalsIgnoreCase("Male")) {
            binding.profileMale.setChecked(true);
        } else if (sGender.equalsIgnoreCase("Female")) {
            binding.profileFemale.setChecked(true);
        }

        sCountry = sp.getString(ConstantSp.COUNTRY, "");
        int iCountryIndex = 0;
        for (int i = 0; i < countryArray.size(); i++) {
            if (countryArray.get(i).equalsIgnoreCase(sCountry)) {
                iCountryIndex = i;
                break;
            }
        }
        binding.profileCountry.setSelection(iCountryIndex);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}