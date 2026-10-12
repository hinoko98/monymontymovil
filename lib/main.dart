import 'dart:async' show unawaited;

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'app.dart';
import 'core/api_client.dart';
import 'core/theme_controller.dart';
import 'features/auth/data/auth_repository.dart';
import 'features/auth/state/auth_controller.dart';
import 'features/onboarding/state/onboarding_controller.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();

  final apiClient = await ApiClient.getInstance();
  final themeController = await ThemeController.load();
  final onboardingController = await OnboardingController.load();
  final authController = AuthController(AuthRepository(apiClient));
  unawaited(authController.bootstrap());

  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider.value(value: authController),
        ChangeNotifierProvider.value(value: themeController),
        ChangeNotifierProvider.value(value: onboardingController),
      ],
      child: const MonyMontyApp(),
    ),
  );
}
