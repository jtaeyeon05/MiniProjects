import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';

import '/view/game_homepage.dart';
import '/view/theme.dart' as theme;
import '/viewmodel/game_view_model.dart';
import '/model/game.dart';


void main() => runApp(
    ChangeNotifierProvider(
        create: (_) => GameViewModel(),
        child: const MyApp()
    )
);


class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}


class _MyAppState extends State<MyApp> {
  final FocusNode _focusNode = FocusNode();
  late GameViewModel _gameViewModel;

  @override
  void initState() {
    super.initState();
    _gameViewModel = Provider.of<GameViewModel>(context, listen: false);
  }

  @override
  Widget build(BuildContext context) {
    _gameViewModel.initThemeMode(MediaQuery.of(context).platformBrightness == Brightness.light ? ThemeMode.light : ThemeMode.dark);
    return Consumer<GameViewModel>(builder: (context, provider, child) {
      return MaterialApp(
        title: 'Flutter2048',
        debugShowCheckedModeBanner: false,
        theme: theme.light(),
        darkTheme: theme.dark(),
        themeMode: provider.themeMode,
        home: KeyboardListener(
          focusNode: _focusNode,
          autofocus: true,
          onKeyEvent: (event) {
            if (event is KeyDownEvent) {
              switch (event.logicalKey) {
                case LogicalKeyboardKey.keyW || LogicalKeyboardKey.arrowUp when _gameViewModel.able(Direction.up):
                  _gameViewModel.process(Direction.up);
                case LogicalKeyboardKey.keyS || LogicalKeyboardKey.arrowDown when _gameViewModel.able(Direction.down):
                  _gameViewModel.process(Direction.down);
                case LogicalKeyboardKey.keyA || LogicalKeyboardKey.arrowLeft when _gameViewModel.able(Direction.left):
                  _gameViewModel.process(Direction.left);
                case LogicalKeyboardKey.keyD || LogicalKeyboardKey.arrowRight when _gameViewModel.able(Direction.right):
                  _gameViewModel.process(Direction.right);
                case LogicalKeyboardKey.shiftLeft || LogicalKeyboardKey.shiftRight when _gameViewModel.free:
                  _gameViewModel.process();
                case LogicalKeyboardKey.keyQ || LogicalKeyboardKey.slash:
                  _gameViewModel.autoProcess();
              }
            }
          },
          child: GameHomePage(),
        ),
      );
    });
  }
}
