import React, {PropsWithChildren} from 'react';
import {SafeAreaView, ScrollView, StyleSheet, Text, View} from 'react-native';
import {Colors} from 'react-native/Libraries/NewAppScreen';
import {useNavigation, useRoute} from '@react-navigation/native';
import {LogResult} from './HomeScreen';
import {Appbar, useTheme} from 'react-native-paper';

type LogProps = PropsWithChildren<{
  title: string;
  json?: string;
}>;

function isJsonString(str: string): boolean {
  try {
    JSON.parse(str);
  } catch (e) {
    return false;
  }
  return true;
}

const LogItem = ({title, json}: LogProps): React.JSX.Element => {
  const theme = useTheme();

  return json != null ? (
    <View style={styles.logContainer}>
      <View
        style={[
          styles.logActionHeader,
          {
            backgroundColor: theme.colors.primary,
          },
        ]}>
        <Text style={styles.logTitle}>{title}</Text>
      </View>
      <Text style={styles.logText}>
        {isJsonString(json)
          ? JSON.stringify(JSON.parse(json.toString()), null, '\t')
          : json}
      </Text>
    </View>
  ) : (
    <View />
  );
};

const LogScreen = (): React.JSX.Element => {
  const route = useRoute();
  const navigation = useNavigation();
  const params: LogResult = route.params as LogResult;
  const theme = useTheme();

  const backgroundStyle = {
    backgroundColor: Colors.white,
    flex: 1,
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <Appbar.Header style={{backgroundColor: theme.colors.primary}}>
        <Appbar.BackAction onPress={navigation.goBack} iconColor={'#ffffff'} />
        <Appbar.Content title="Hiển thị kết quả" color={'#ffffff'} />
      </Appbar.Header>
      <ScrollView>
        <View style={styles.spacer} />
        <LogItem title="Avatar NFC" json={params.imageAvatarCardNfc} />
        <LogItem title="Client session" json={params.clientSessionResult} />
        <LogItem title="Log NFC" json={params.logNfc} />
        <LogItem title="Hash avatar" json={params.hashAvatar} />
        <LogItem
          title="Postcode original location"
          json={params.postCodeOriginalLocation}
        />
        <LogItem
          title="Postcode recent location"
          json={params.postCodeRecentLocation}
        />
        <LogItem title="Time scan NFC" json={params.timeScanNfc} />
        <LogItem title="Check auth chip" json={params.checkAuthChipResult} />
        <LogItem title="Qrcode" json={params.qrCodeResult} />
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  sectionTitle: {
    fontSize: 18,
    fontWeight: '600',
    color: Colors.black,
    textAlign: 'center',
    flex: 1,
    marginHorizontal: 8,
  },
  sectionAction: {
    marginHorizontal: 8,
    padding: 6,
    fontSize: 16,
    fontWeight: '600',
    color: Colors.black,
    textAlign: 'center',
  },
  logContainer: {
    flexDirection: 'column',
  },
  logActionHeader: {
    flexDirection: 'row',
    padding: 8,
    justifyContent: 'center',
    alignItems: 'center',
  },
  logTitle: {
    fontSize: 16,
    color: Colors.white,
    flex: 1,
  },
  logText: {
    fontSize: 14,
    padding: 8,
    color: Colors.black,
  },
  spacer: {
    height: 8,
  },
});

export default LogScreen;
