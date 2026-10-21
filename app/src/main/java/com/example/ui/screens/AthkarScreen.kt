package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DhikrItem
import com.example.data.SampleData
import com.example.ui.components.ArabesqueBackgroundPattern
import com.example.ui.components.toArabicNumerals
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun AthkarScreen(
  onShowMessage: (String) -> Unit = {}
) {
  var searchQuery by remember { mutableStateOf("") }
  var tasbeehCount by remember { mutableIntStateOf(33) }
  var activePopupCategory by remember { mutableStateOf<String?>(null) }

  // Athkar Popup Dialog when a category is selected (Replaces accordion animation)
  activePopupCategory?.let { category ->
    AthkarCategoryPopupDialog(
      categoryName = category,
      onDismiss = { activePopupCategory = null },
      onShowMessage = onShowMessage
    )
  }

  Box(modifier = Modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      item { Spacer(modifier = Modifier.height(4.dp)) }

      // 1. Search Bar
      item {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = {
            Text(
              text = "ابحث في الأذكار، الأدعية، والفضائل...",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
          },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary
            )
          },
          singleLine = true,
          shape = RoundedCornerShape(16.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("athkar_search_field")
        )
      }

      // 2. Smart Tasbih Widget
      item {
        Card(
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("smart_tasbih_card")
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondaryFixed,
                    modifier = Modifier.size(16.dp)
                  )
                }
                Text(
                  text = "المسبحة الإلكترونية السريعة",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.primaryFixed,
                  fontWeight = FontWeight.Bold
                )
              }

              Text(
                text = "إعادة",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondaryFixed,
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color.White.copy(alpha = 0.12f))
                  .clickable { tasbeehCount = 0 }
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
                  style = MaterialTheme.typography.titleMedium.copy(fontFamily = FontFamily.Serif),
                  color = Color.White,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "الهدف اليومي: ١٠٠ تسبيحة",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.primaryFixedDim
                )
              }

              Text(
                text = toArabicNumerals(tasbeehCount),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.secondaryFixed,
                fontWeight = FontWeight.Bold
              )
            }

            // Tap Button
            var isPressed by remember { mutableStateOf(false) }
            val scale by animateFloatAsState(targetValue = if (isPressed) 0.94f else 1f, label = "tasbih_scale")

            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "اضغط للعد والتسبيح في أي وقت",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primaryFixedDim
              )

              Box(
                modifier = Modifier
                  .scale(scale)
                  .clip(RoundedCornerShape(12.dp))
                  .background(MaterialTheme.colorScheme.secondaryContainer)
                  .clickable {
                    isPressed = true
                    tasbeehCount++
                  }
                  .padding(horizontal = 16.dp, vertical = 8.dp)
                  .testTag("tasbeeh_tap_button"),
                contentAlignment = Alignment.Center
              ) {
                LaunchedEffect(isPressed) {
                  if (isPressed) {
                    delay(100)
                    isPressed = false
                  }
                }
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Text(
                    text = "سبّح الآن",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.Bold
                  )
                  Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(16.dp)
                  )
                }
              }
            }
          }
        }
      }

      // 3. Four Athkar Main Category Cards
      item {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          // Card 1: قسم الأدعية
          CategoryCard(
            title = "قسم الأدعية",
            subtitle = "أدعية القرآن الكريم والسنة النبوية وتفريج الهموم",
            icon = Icons.Default.MenuBook,
            isSelected = activePopupCategory == "قسم الأدعية",
            onClick = {
              activePopupCategory = "قسم الأدعية"
            }
          )

          // Card 2: قسم الأذكار
          CategoryCard(
            title = "أذكار الصباح والمساء",
            subtitle = "الحصن الحصين والأذكار المأثورة لكل صباح ومساء",
            icon = Icons.Default.SelfImprovement,
            isSelected = activePopupCategory == "أذكار الصباح والمساء",
            onClick = {
              activePopupCategory = "أذكار الصباح والمساء"
            }
          )

          // Card 3: أذكاري المخصصة
          CategoryCard(
            title = "أذكاري المخصصة",
            subtitle = "أضف أذكارك الخاصة مع عداد تسبيح شخصي",
            icon = Icons.Default.Edit,
            isSelected = activePopupCategory == "أذكاري المخصصة",
            onClick = {
              activePopupCategory = "أذكاري المخصصة"
            }
          )

          // Card 4: قسم الفضائل
          CategoryCard(
            title = "قسم الفضائل",
            subtitle = "فضل الذكر، الاستغفار، والصلاة على النبي ﷺ",
            icon = Icons.Default.AutoAwesome,
            isSelected = activePopupCategory == "قسم الفضائل",
            onClick = {
              activePopupCategory = "قسم الفضائل"
            }
          )
        }
      }

      // 4. Sub-category Grid (المسبحة & الرقية الشرعية)
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // Grid Item 1: المسبحة
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
              .weight(1f)
              .clickable { activePopupCategory = "المسبحة الإلكترونية الشاملة" }
              .testTag("subcat_masbaha")
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              Box(
                modifier = Modifier
                  .size(46.dp)
                  .clip(CircleShape)
                  .background(GoldLight),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Timer,
                  contentDescription = null,
                  tint = GoldAccent,
                  modifier = Modifier.size(24.dp)
                )
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "المسبحة",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "فتح شاشة العداد",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          // Grid Item 2: الرقية الشرعية
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
              .weight(1f)
              .clickable { activePopupCategory = "الرقية الشرعية" }
              .testTag("subcat_roqya")
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              Box(
                modifier = Modifier
                  .size(46.dp)
                  .clip(CircleShape)
                  .background(GoldLight),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Shield,
                  contentDescription = null,
                  tint = GoldAccent,
                  modifier = Modifier.size(24.dp)
                )
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "الرقية الشرعية",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "فتح شاشة التحصين",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      item { Spacer(modifier = Modifier.height(80.dp)) }
    }
  }
}

@Composable
fun CategoryCard(
  title: String,
  subtitle: String,
  icon: ImageVector,
  isSelected: Boolean = false,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceContainer
      else MaterialTheme.colorScheme.surfaceContainerLow
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = Modifier
      .fillMaxWidth()
      .border(
        width = 1.dp,
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFBCE3D9).copy(alpha = 0.5f),
        shape = RoundedCornerShape(20.dp)
      )
      .clickable(onClick = onClick)
      .testTag("category_card_${title}")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Left chevron
      Icon(
        imageVector = Icons.Default.ChevronLeft,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(22.dp)
      )

      // Center title & description
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center
        )
      }

      // Circular icon badge
      Box(
        modifier = Modifier
          .size(50.dp)
          .clip(CircleShape)
          .background(Color(0xFFCBEBE3)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(24.dp)
        )
      }
    }
  }
}

@Composable
fun DhikrCard(
  item: DhikrItem,
  onDecrement: () -> Unit,
  onReset: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("dhikr_card_${item.id}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Text(
        text = item.text,
        style = MaterialTheme.typography.bodyLarge.copy(
          fontFamily = FontFamily.Serif,
          fontSize = 17.sp,
          lineHeight = 30.sp
        ),
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold
      )

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(MaterialTheme.colorScheme.surfaceContainerLow)
          .padding(8.dp)
      ) {
        Text(
          text = "الفضل: ${item.virtue}",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.secondary
        )
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          IconButton(
            onClick = onReset,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = "إعادة التعيين",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(18.dp)
            )
          }
          Text(
            text = "العدد الكلي: ${toArabicNumerals(item.initialCount)}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        // Decrement button / Completion check
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
              if (item.currentCount == 0) MaterialTheme.colorScheme.tertiaryFixed
              else MaterialTheme.colorScheme.primary
            )
            .clickable(onClick = onDecrement)
            .padding(horizontal = 18.dp, vertical = 8.dp)
            .testTag("dhikr_counter_btn_${item.id}"),
          contentAlignment = Alignment.Center
        ) {
          if (item.currentCount == 0) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
              )
              Text(
                text = "تم بحمد الله",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
              )
            }
          } else {
            Text(
              text = "${toArabicNumerals(item.currentCount)}",
              style = MaterialTheme.typography.titleMedium,
              color = Color.White,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
