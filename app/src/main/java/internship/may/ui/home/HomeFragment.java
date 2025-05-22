package internship.may.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import internship.may.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    String[] nameArray = {"Kilos","Mobiles","Fashion","Electronics","Home & Furniture","Appliances","Flight Bookings","Beauty, Toys & More","Two Wheelers"};
    String[] imageArray = {
            "https://rukminim2.flixcart.com/flap/80/80/image/29327f40e9c4d26b.png?q=100",
            "https://rukminim2.flixcart.com/flap/80/80/image/22fddf3c7da4c4f4.png?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/80/80/image/0d75b34f7d8fbcb3.png?q=100",
            "https://rukminim2.flixcart.com/flap/80/80/image/69c6589653afdb9a.png?q=100",
            "https://rukminim2.flixcart.com/flap/80/80/image/69c6589653afdb9a.png?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/80/80/image/0139228b2f7eb413.jpg?q=100",
            "https://rukminim2.flixcart.com/flap/80/80/image/71050627a56b4693.png?q=100",
            "https://rukminim2.flixcart.com/flap/80/80/image/dff3f7adcf3a90c6.png?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/80/80/image/05d708653beff580.png?q=100"
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}