<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="6dp"
    app:cardBackgroundColor="@color/colorCard"
    app:cardCornerRadius="16dp"
    app:cardElevation="4dp"
    app:strokeColor="@color/colorBorder"
    app:strokeWidth="1dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <FrameLayout
            android:layout_width="match_parent"
            android:layout_height="140dp">

            <ImageView
                android:id="@+id/ivRecipeImage"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:scaleType="centerCrop"
                android:src="@drawable/placeholder_recipe" />

            <View
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:background="@drawable/gradient_image_overlay" />

            <TextView
                android:id="@+id/tvMilletType"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_margin="8dp"
                android:textColor="@color/white"
                android:textSize="10sp"
                android:textStyle="bold"
                android:paddingHorizontal="8dp"
                android:paddingVertical="3dp"
                android:background="@drawable/bg_millet_tag" />

            <ImageView
                android:id="@+id/ivFavorite"
                android:layout_width="32dp"
                android:layout_height="32dp"
                android:layout_gravity="top|end"
                android:layout_margin="8dp"
                android:src="@drawable/ic_heart_outline"
                android:background="@drawable/bg_circle_white"
                android:padding="6dp" />

            <TextView
                android:id="@+id/tvCategory"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="bottom|start"
                android:layout_margin="8dp"
                android:textColor="@color/white"
                android:textSize="9sp"
                android:paddingHorizontal="6dp"
                android:paddingVertical="2dp"
                android:background="@drawable/bg_dark_badge" />
        </FrameLayout>

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:padding="10dp">

            <TextView
                android:id="@+id/tvRecipeName"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:textColor="@color/colorTextPrimary"
                android:textSize="14sp"
                android:textStyle="bold"
                android:maxLines="1"
                android:ellipsize="end" />

            <TextView
                android:id="@+id/tvKannadaName"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:textColor="@color/colorAccent"
                android:textSize="13sp"
                android:layout_marginTop="1dp" />

            <TextView
                android:id="@+id/tvHealthTag"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textColor="@color/colorSecondary"
                android:textSize="9sp"
                android:textStyle="bold"
                android:paddingHorizontal="6dp"
                android:paddingVertical="2dp"
                android:background="@drawable/bg_health_tag"
                android:layout_marginTop="4dp" />

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:layout_marginTop="8dp">

                <TextView
                    android:id="@+id/tvCookTime"
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:textColor="@color/colorTextHint"
                    android:textSize="10sp" />

                <TextView
                    android:id="@+id/tvServings"
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:textColor="@color/colorTextHint"
                    android:textSize="10sp" />
            </LinearLayout>
        </LinearLayout>
    </LinearLayout>
</androidx.cardview.widget.CardView>