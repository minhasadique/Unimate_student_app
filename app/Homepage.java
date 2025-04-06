public class Homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Find the TextView
        TextView headerTitle = findViewById(R.id.headerTitle);

        // Create the SpannableString for "UNIMATE"
        SpannableString spannableString = new SpannableString("UNIMATE");

        // Apply color to "UNI"
        spannableString.setSpan(
                new ForegroundColorSpan(Color.parseColor("#4282aa")), // Color for "UNI"
                0, // Start index
                3, // End index (exclusive)
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Apply color to "MATE"
        spannableString.setSpan(
                new ForegroundColorSpan(Color.parseColor("#23cbc3")), // Color for "MATE"
                3, // Start index
                7, // End index (exclusive)
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Set the styled text to the TextView
        headerTitle.setText(spannableString);

        // Navigate to ProfilePage when profile icon is clicked
        ImageView profileIcon = findViewById(R.id.profileIcon);
        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, ProfilePage.class);
            startActivity(intent);
        });

        // Navigate to SearchActivity when search icon is clicked
        ImageView searchIcon = findViewById(R.id.settingsIcon); // Assuming settingsIcon is the search logo
        searchIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, SearchActivity.class);
            startActivity(intent);
        });

        // Navigate to StartupActivity when startup icon is clicked
        ImageView startupIcon = findViewById(R.id.notificationIcon); // Assuming notificationIcon is for startup
        startupIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, StartupActivity.class);
            startActivity(intent);
        });
    }
}