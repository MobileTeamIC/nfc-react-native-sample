/* eslint-disable react-native/no-inline-styles */
import * as React from 'react';
import {
  NavigationContainer,
  RouteConfig,
  StackNavigationState,
} from '@react-navigation/native';
import {
  createNativeStackNavigator,
  NativeStackNavigationEventMap,
  NativeStackNavigationOptions,
} from '@react-navigation/native-stack';
import HomeScreen, {LogResult} from './HomeScreen';
import LogScreen from './LogScreen';
import {MD3LightTheme, PaperProvider} from 'react-native-paper';
import {SafeAreaView, StatusBar} from 'react-native';

// stack param list type
export type AppStackParamList = {
  Home: undefined;
  Log: LogResult | undefined;
};

// type of the single route in app stack
type AppStackRoutesType = RouteConfig<
  AppStackParamList,
  keyof AppStackParamList,
  StackNavigationState<AppStackParamList>,
  NativeStackNavigationOptions,
  NativeStackNavigationEventMap
>;

// strictly typed array of app stack routes
const appStackRoutes: Array<AppStackRoutesType> = [
  {
    name: 'Home',
    component: HomeScreen,
  },
  {
    name: 'Log',
    component: LogScreen,
  },
];

const Stack = createNativeStackNavigator<AppStackParamList>();

const theme = {
  ...MD3LightTheme,
  colors: {
    ...MD3LightTheme.colors,
  },
};

const App = (): React.JSX.Element => {
  return (
    <PaperProvider theme={theme}>
      <SafeAreaView style={{flex: 1, backgroundColor: theme.colors.background}}>
        <StatusBar
          barStyle={!theme.dark ? 'light-content' : 'dark-content'}
          backgroundColor={theme.colors.primary}
        />
        <NavigationContainer>
          <Stack.Navigator
            initialRouteName="Home"
            screenOptions={{
              headerShown: false,
            }}>
            {appStackRoutes.map(stackRoute => (
              <Stack.Screen key={stackRoute.name} {...stackRoute} />
            ))}
          </Stack.Navigator>
        </NavigationContainer>
      </SafeAreaView>
    </PaperProvider>
  );
};

export default App;
