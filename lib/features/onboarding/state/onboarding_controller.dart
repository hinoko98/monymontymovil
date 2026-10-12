import 'package:flutter/foundation.dart';
import 'package:shared_preferences/shared_preferences.dart';

/// Recuerda si el usuario ya vio las pantallas de presentación, para que
/// solo aparezcan la primera vez que abre la app.
class OnboardingController extends ChangeNotifier {
  OnboardingController._(this._prefs, this._seen);

  static const _key = 'onboarding_seen';

  final SharedPreferences _prefs;
  bool _seen;

  bool get seen => _seen;

  static Future<OnboardingController> load() async {
    final prefs = await SharedPreferences.getInstance();
    return OnboardingController._(prefs, prefs.getBool(_key) ?? false);
  }

  Future<void> complete() async {
    if (_seen) return;
    _seen = true;
    notifyListeners();
    await _prefs.setBool(_key, true);
  }
}
