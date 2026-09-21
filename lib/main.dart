import 'dart:async' show unawaited;

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'app.dart';
import 'core/api_client.dart';
import 'features/auth/data/auth_repository.dart';
import 'features/auth/state/auth_controller.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();

  final apiClient = await ApiClient.getInstance();
  final authController = AuthController(AuthRepository(apiClient));
  unawaited(authController.bootstrap());

  runApp(
    ChangeNotifierProvider.value(
      value: authController,
      child: const MonyMontyApp(),
    ),
  );
}
