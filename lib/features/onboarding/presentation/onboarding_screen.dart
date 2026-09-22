import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../../core/app_theme.dart';
import '../../../core/widgets/app_fields.dart';
import '../../../core/widgets/brand_lockup.dart';
import '../state/onboarding_controller.dart';

/// Las tres pantallas que presentan la app la primera vez que se abre.
class OnboardingScreen extends StatefulWidget {
  const OnboardingScreen({super.key});

  @override
  State<OnboardingScreen> createState() => _OnboardingScreenState();
}

class _OnboardingScreenState extends State<OnboardingScreen> {
  final _pageController = PageController();
  int _page = 0;

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  List<_Slide> _slides(AppColors colors) => [
    _Slide(
      icon: Icons.account_balance_wallet_outlined,
      accent: colors.primary,
      softBackground: colors.softGreen,
      titleTop: 'Tu dinero,',
      titleLeading: '',
      titleAccent: 'bajo control',
      accentColor: colors.brand,
      subtitle:
          'Gestiona tus finanzas personales de forma fácil, rápida y segura desde tu celular.',
    ),
    _Slide(
      icon: Icons.groups_outlined,
      accent: colors.amber,
      softBackground: colors.softAmber,
      titleTop: 'Asesoría',
      titleLeading: '',
      titleAccent: 'a tu medida',
      accentColor: colors.amber,
      subtitle:
          'Recibe orientación personalizada y aprende en sesiones grupales junto a otros usuarios.',
    ),
    _Slide(
      icon: Icons.bar_chart_rounded,
      accent: colors.blue,
      softBackground: colors.softBlue,
      titleTop: 'Ahorra e invierte',
      titleLeading: 'con ',
      titleAccent: 'inteligencia',
      accentColor: colors.blue,
      subtitle:
          'Aplica teorías económicas actualizadas para maximizar tu ahorro y tus inversiones.',
    ),
  ];

  void _finish() => context.read<OnboardingController>().complete();

  void _next(int total) {
    if (_page >= total - 1) {
      _finish();
      return;
    }
    _pageController.nextPage(
      duration: const Duration(milliseconds: 280),
      curve: Curves.easeOut,
    );
  }

  void _back() => _pageController.previousPage(
    duration: const Duration(milliseconds: 280),
    curve: Curves.easeOut,
  );

  @override
  Widget build(BuildContext context) {
    final colors = AppColors.of(context);
    final slides = _slides(colors);
    final isLast = _page == slides.length - 1;

    return Scaffold(
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.fromLTRB(24, 12, 24, 24),
          child: Column(
            children: [
              SizedBox(
                height: 52,
                child: Row(
                  children: [
                    Expanded(
                      child: Align(
                        alignment: Alignment.centerLeft,
                        // Si el nombre no cabe (pantallas angostas o fuentes
                        // grandes) se encoge en vez de desbordarse.
                        child: FittedBox(
                          fit: BoxFit.scaleDown,
                          alignment: Alignment.centerLeft,
                          child: _page == 0
                              ? const BrandLockup(markSize: 40, fontSize: 21)
                              : BackSquareButton(onPressed: _back),
                        ),
                      ),
                    ),
                    if (!isLast)
                      TextButton(
                        onPressed: _finish,
                        style: TextButton.styleFrom(
                          foregroundColor: colors.muted,
                        ),
                        child: const Text('Omitir'),
                      ),
                  ],
                ),
              ),
              const SizedBox(height: 12),
              Expanded(
                child: PageView.builder(
                  controller: _pageController,
                  itemCount: slides.length,
                  onPageChanged: (index) => setState(() => _page = index),
                  itemBuilder: (context, index) => _SlideView(slides[index]),
                ),
              ),
              const SizedBox(height: 16),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  for (var i = 0; i < slides.length; i++)
                    AnimatedContainer(
                      duration: const Duration(milliseconds: 220),
                      margin: const EdgeInsets.symmetric(horizontal: 4),
                      width: i == _page ? 24 : 8,
                      height: 8,
                      decoration: BoxDecoration(
                        color: i == _page ? colors.primary : colors.border,
                        borderRadius: BorderRadius.circular(4),
                      ),
                    ),
                ],
              ),
              const SizedBox(height: 20),
              SizedBox(
                width: double.infinity,
                child: FilledButton(
                  onPressed: () => _next(slides.length),
                  child: Text(isLast ? 'Comenzar' : 'Siguiente'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _Slide {
  const _Slide({
    required this.icon,
    required this.accent,
    required this.softBackground,
    required this.titleTop,
    required this.titleLeading,
    required this.titleAccent,
    required this.accentColor,
    required this.subtitle,
  });

  final IconData icon;

  /// Color del cuadro que contiene el ícono.
  final Color accent;
  final Color softBackground;
  final String titleTop;

  /// Parte de la segunda línea que va en el color del texto normal.
  final String titleLeading;
  final String titleAccent;

  /// Color de la palabra destacada del título.
  final Color accentColor;
  final String subtitle;
}

class _SlideView extends StatelessWidget {
  const _SlideView(this.slide);

  final _Slide slide;

  @override
  Widget build(BuildContext context) {
    final colors = AppColors.of(context);

    return Column(
      children: [
        Expanded(
          child: Container(
            width: double.infinity,
            decoration: BoxDecoration(
              color: slide.softBackground,
              borderRadius: BorderRadius.circular(24),
            ),
            child: Center(
              child: Container(
                width: 108,
                height: 108,
                decoration: BoxDecoration(
                  color: slide.accent,
                  borderRadius: BorderRadius.circular(28),
                ),
                child: Icon(slide.icon, size: 52, color: Colors.white),
              ),
            ),
          ),
        ),
        const SizedBox(height: 32),
        Text.rich(
          TextSpan(
            style: TextStyle(
              fontSize: 26,
              fontWeight: FontWeight.w800,
              height: 1.25,
              color: colors.title,
            ),
            children: [
              TextSpan(text: '${slide.titleTop}\n'),
              TextSpan(text: slide.titleLeading),
              TextSpan(
                text: slide.titleAccent,
                style: TextStyle(color: slide.accentColor),
              ),
            ],
          ),
          textAlign: TextAlign.center,
        ),
        const SizedBox(height: 12),
        Text(
          slide.subtitle,
          textAlign: TextAlign.center,
          style: TextStyle(fontSize: 15, height: 1.5, color: colors.muted),
        ),
        const Spacer(),
      ],
    );
  }
}
