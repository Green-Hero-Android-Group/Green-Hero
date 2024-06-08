    package com.example.green_hero.ui.recycle;
    
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.RadioGroup;
    import android.widget.Toast;
    
    import androidx.annotation.NonNull;
    import androidx.fragment.app.Fragment;
    import androidx.lifecycle.ViewModelProvider;

    import com.example.green_hero.R;
    import com.example.green_hero.databinding.FragmentRecycleBinding;
    import com.example.green_hero.model.Recycle.Item;

    import org.bson.BSON;
    import org.bson.BsonNumber;
    import org.bson.codecs.IntegerCodec;

    public class RecycleFragment extends Fragment {
        private FragmentRecycleBinding binding;
        private Button recycleButton;

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            binding = FragmentRecycleBinding.inflate(inflater, container, false);
            View root;
            root = binding.getRoot();

            recycleButton = root.findViewById(R.id.signUpButton);
            recycleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickRecycle();
                }
            });

            return root;
        }
    
        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }

        //Click listener for the recycle button
        public void onClickRecycle() {
            //DB
            RecycleViewModel viewModel = new ViewModelProvider(this).get(RecycleViewModel.class);
            EditText name = getView().findViewById(R.id.type_name_input);
            String typeName = name.getText().toString().trim();
            EditText qntInput = getView().findViewById(R.id.quantity);
            int qnt = 0;
            if (!qntInput.getText().toString().isEmpty()) {
                qnt = Integer.parseInt(qntInput.getText().toString());
            }

            //Getting the selected radio button
            RadioGroup rg = getView().findViewById(R.id.radioGroup);
            int checkedButton = rg.getCheckedRadioButtonId();

            //Checking if all fields are filled
            if(checkedButton == -1 || typeName.isEmpty() || qntInput.getText().toString().isEmpty()){
                Toast.makeText(getActivity(),
                        "Please fill all the fields", Toast.LENGTH_SHORT).show();
            } else{
                String selected = "";
                switch (checkedButton){
                    case R.id.paper_button:
                        selected = "Paper";
                        break;
                    case R.id.plastic_button:
                        selected = "Plastic";
                        break;
                    case R.id.glass_button:
                        selected = "Glass";
                        break;
                }

                //Creating a new item
                Item newItem = new Item(typeName, qnt, selected);

                Toast.makeText(getActivity(),
                        "You Recycled: " + newItem.getType() + "\nName: " + newItem.getName() + "\nQuantity: " + newItem.getQuantity(), Toast.LENGTH_SHORT).show();
                //Inserting the item into the database
                viewModel.insertItem(newItem);

                //Clearing the fields
                name.getText().clear();
                qntInput.getText().clear();
                rg.clearCheck();
            }
        }
    }


